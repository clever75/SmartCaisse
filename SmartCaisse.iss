; ============================================================
; SmartCaisse - installateur complet
; Installe l'application (Java embarqué) + MariaDB (service Windows)
; ============================================================
#define AppNom     "SmartCaisse"
#define AppVersion "1.0"
#define Staging    "C:\Users\Clever\Desktop\SmartCaisse\installer\staging"

[Setup]
AppId={{3F9A6D12-8B47-4C05-A1E3-7D2C5B90F486}
AppName={#AppNom}
AppVersion={#AppVersion}
AppPublisher=Clever
DefaultDirName={autopf}\{#AppNom}
DefaultGroupName={#AppNom}
DisableProgramGroupPage=yes
OutputDir=C:\Users\Clever\Desktop\SmartCaisse\installer
OutputBaseFilename=SmartCaisse_Setup_v{#AppVersion}
SetupIconFile={#Staging}\icone.ico
UninstallDisplayIcon={app}\icone.ico
Compression=lzma2
SolidCompression=yes
PrivilegesRequired=admin
ArchitecturesAllowed=x64compatible
ArchitecturesInstallIn64BitMode=x64compatible
WizardStyle=modern

[Languages]
Name: "french"; MessagesFile: "compiler:Languages\French.isl"

[Tasks]
Name: "desktopicon"; Description: "Créer un raccourci sur le Bureau"; GroupDescription: "Raccourcis :"

[Files]
Source: "{#Staging}\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{autodesktop}\{#AppNom}"; Filename: "{app}\{#AppNom}.exe"; WorkingDir: "{app}"; IconFilename: "{app}\icone.ico"; Tasks: desktopicon
Name: "{group}\{#AppNom}"; Filename: "{app}\{#AppNom}.exe"; WorkingDir: "{app}"; IconFilename: "{app}\icone.ico"
Name: "{group}\Désinstaller {#AppNom}"; Filename: "{uninstallexe}"

[Run]
Filename: "{app}\{#AppNom}.exe"; Description: "Lancer {#AppNom}"; WorkingDir: "{app}"; Flags: nowait postinstall skipifsilent

[UninstallDelete]
Type: filesandordirs; Name: "{app}"

[Code]
const
  NomService      = 'SmartCaisseDB';
  PortBase        = '3308';
  NomBase         = 'smartcaisse';
  UtilisateurBase = 'smartcaisse';

// Générateur aléatoire de Windows (BCryptGenRandom), plus sûr que Random()
function BCryptGenRandom(hAlgorithm: Cardinal; var pbBuffer: Byte;
  cbBuffer: Cardinal; dwFlags: Cardinal): Longint;
  external 'BCryptGenRandom@bcrypt.dll stdcall';

function MotDePasseAleatoire(Longueur: Integer): String;
var
  Alphabet: String;
  B: Byte;
  Limite: Integer;
begin
  // Sans caractères ambigus (0/O, 1/l/I) ni caractères spéciaux
  Alphabet := 'abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789';
  Limite := 256 - (256 mod Length(Alphabet));
  Result := '';
  while Length(Result) < Longueur do
  begin
    if BCryptGenRandom(0, B, 1, 2) <> 0 then
      RaiseException('Génération du mot de passe impossible.');
    if B < Limite then
      Result := Result + Alphabet[(B mod Length(Alphabet)) + 1];
  end;
end;

function ServiceNet(const Verbe: String): Boolean;
var
  Code: Integer;
begin
  Result := Exec(ExpandConstant('{sys}\net.exe'), Verbe + ' ' + NomService,
    '', SW_HIDE, ewWaitUntilTerminated, Code) and (Code = 0);
end;

function ServiceExiste: Boolean;
var
  Code: Integer;
begin
  Result := Exec(ExpandConstant('{sys}\sc.exe'), 'query ' + NomService,
    '', SW_HIDE, ewWaitUntilTerminated, Code) and (Code = 0);
end;

function InstallerService(const Ini: String): Boolean;
var
  Code: Integer;
begin
  Result := Exec(ExpandConstant('{app}\mariadb\bin\mariadbd.exe'),
    '--install ' + NomService + ' --defaults-file="' + Ini + '"',
    '', SW_HIDE, ewWaitUntilTerminated, Code) and (Code = 0);
end;

// Lance le client mariadb.exe en root ; Parametres = redirection ou option -e
function ClientSql(const MdpRoot, Parametres: String): Boolean;
var
  Code: Integer;
begin
  Result := Exec(ExpandConstant('{cmd}'),
    '/S /C ""' + ExpandConstant('{app}\mariadb\bin\mariadb.exe') +
    '" -u root -p' + MdpRoot + ' -h 127.0.0.1 -P ' + PortBase + ' ' + Parametres + '"',
    '', SW_HIDE, ewWaitUntilTerminated, Code) and (Code = 0);
end;

function AttendreBase(const MdpRoot: String): Boolean;
var
  I: Integer;
begin
  Result := False;
  for I := 1 to 60 do
  begin
    if ClientSql(MdpRoot, '-e "SELECT 1" >NUL 2>&1') then
    begin
      Result := True;
      Exit;
    end;
    Sleep(500);
  end;
end;

function CompteSql(const Hote: String): String;
begin
  Result := '''' + UtilisateurBase + '''@''' + Hote + '''';
end;

procedure EchecBase(const Etape, Donnees: String);
var
  Code: Integer;
begin
  // On remet à zéro pour qu'une nouvelle tentative reparte proprement
  ServiceNet('stop');
  Exec(ExpandConstant('{sys}\sc.exe'), 'delete ' + NomService, '', SW_HIDE, ewWaitUntilTerminated, Code);
  DelTree(Donnees, True, True, True);
  MsgBox('L''installation de la base de données a échoué à l''étape :' + #13#10 +
    Etape + #13#10 + #13#10 +
    'L''application est installée mais ne pourra pas fonctionner. ' +
    'Relancez l''installation ; si le problème persiste, contactez le support.',
    mbError, MB_OK);
end;

procedure ConfigurerBase;
var
  Racine, Donnees, DonneesIni, Ini, Cfg, Sauvegarde, SqlUtil: String;
  MdpRoot, MdpApp, Texte: String;
  Code: Integer;
begin
  Racine := ExpandConstant('{commonappdata}\SmartCaisse');
  Donnees := Racine + '\data';
  Ini := Donnees + '\my.ini';
  Cfg := ExpandConstant('{app}\app\config.properties');
  Sauvegarde := Racine + '\config.properties.sauvegarde';

  // Cas 1 : des données existent déjà (mise à jour ou réinstallation) -> on les garde
  if DirExists(Donnees + '\mysql') then
  begin
    if (not FileExists(Cfg)) and FileExists(Sauvegarde) then
      FileCopy(Sauvegarde, Cfg, False);
    if not FileExists(Cfg) then
    begin
      MsgBox('Des données existantes ont été conservées, mais leur fichier de ' +
        'configuration est introuvable. L''application ne pourra pas se connecter.',
        mbError, MB_OK);
      Exit;
    end;
    if not ServiceExiste then
      InstallerService(Ini);
    ServiceNet('start');
    Exit;
  end;

  // Cas 2 : première installation
  ForceDirectories(Racine);
  MdpRoot := MotDePasseAleatoire(24);
  MdpApp := MotDePasseAleatoire(24);

  // 1. Initialisation du dossier de données
  if not (Exec(ExpandConstant('{app}\mariadb\bin\mysql_install_db.exe'),
      '--datadir="' + Donnees + '" --port=' + PortBase + ' --password=' + MdpRoot,
      '', SW_HIDE, ewWaitUntilTerminated, Code) and (Code = 0)) then
  begin
    EchecBase('initialisation des données', Donnees);
    Exit;
  end;

  // 2. Configuration du serveur : local uniquement, UTF-8
  DonneesIni := Donnees;
  StringChangeEx(DonneesIni, '\', '/', True);
  Texte := '[mysqld]' + #13#10 +
    'datadir=' + DonneesIni + #13#10 +
    'port=' + PortBase + #13#10 +
    'bind-address=127.0.0.1' + #13#10 +
    'character-set-server=utf8mb4' + #13#10 +
    'collation-server=utf8mb4_general_ci' + #13#10 + #13#10 +
    '[client]' + #13#10 +
    'port=' + PortBase + #13#10;
  SaveStringToFile(Ini, Texte, False);

  // 3. Service Windows, démarré automatiquement avec le PC
  if not InstallerService(Ini) then
  begin
    EchecBase('création du service Windows', Donnees);
    Exit;
  end;
  if not ServiceNet('start') then
  begin
    EchecBase('démarrage du service (le port ' + PortBase + ' est peut-être déjà utilisé)', Donnees);
    Exit;
  end;
  if not AttendreBase(MdpRoot) then
  begin
    EchecBase('connexion au serveur', Donnees);
    Exit;
  end;

  // 4. Tables, puis compte de démonstration
  if not ClientSql(MdpRoot, '< "' + ExpandConstant('{app}\sql\schema.sql') + '"') then
  begin
    EchecBase('création des tables', Donnees);
    Exit;
  end;
  if not ClientSql(MdpRoot, '< "' + ExpandConstant('{app}\sql\seed.sql') + '"') then
  begin
    EchecBase('création du compte de démonstration', Donnees);
    Exit;
  end;

  // 5. Compte dédié à l'application, limité à sa seule base
  SqlUtil := ExpandConstant('{tmp}\utilisateur.sql');
  Texte :=
    'CREATE USER ' + CompteSql('localhost') + ' IDENTIFIED BY ''' + MdpApp + ''';' + #13#10 +
    'CREATE USER ' + CompteSql('127.0.0.1') + ' IDENTIFIED BY ''' + MdpApp + ''';' + #13#10 +
    'GRANT ALL PRIVILEGES ON ' + NomBase + '.* TO ' + CompteSql('localhost') + ';' + #13#10 +
    'GRANT ALL PRIVILEGES ON ' + NomBase + '.* TO ' + CompteSql('127.0.0.1') + ';' + #13#10 +
    'FLUSH PRIVILEGES;' + #13#10;
  SaveStringToFile(SqlUtil, Texte, False);
  if not ClientSql(MdpRoot, '< "' + SqlUtil + '"') then
  begin
    DeleteFile(SqlUtil);
    EchecBase('création du compte de l''application', Donnees);
    Exit;
  end;
  DeleteFile(SqlUtil);

  // 6. Fichier de configuration lu par l'application
  Texte := 'db.host=127.0.0.1' + #13#10 +
    'db.port=' + PortBase + #13#10 +
    'db.name=' + NomBase + #13#10 +
    'db.user=' + UtilisateurBase + #13#10 +
    'db.password=' + MdpApp + #13#10;
  SaveStringToFile(Cfg, Texte, False);
end;

function PrepareToInstall(var NeedsRestart: Boolean): String;
begin
  Result := '';
  // En cas de mise à jour, le serveur doit être arrêté pour remplacer ses fichiers
  ServiceNet('stop');
end;

procedure CurStepChanged(CurStep: TSetupStep);
begin
  if CurStep = ssPostInstall then
    ConfigurerBase;
end;

procedure CurUninstallStepChanged(CurUninstallStep: TUninstallStep);
var
  Racine, Cfg: String;
  Code: Integer;
  Supprimer: Boolean;
begin
  if CurUninstallStep = usUninstall then
  begin
    ServiceNet('stop');
    Exec(ExpandConstant('{sys}\sc.exe'), 'delete ' + NomService, '', SW_HIDE, ewWaitUntilTerminated, Code);

    Racine := ExpandConstant('{commonappdata}\SmartCaisse');
    Cfg := ExpandConstant('{app}\app\config.properties');
    Supprimer := (not UninstallSilent) and
      (MsgBox('Voulez-vous aussi supprimer les données (la base) de SmartCaisse ?' + #13#10 + #13#10 +
       'Choisissez Non pour les conserver (recommandé si vous comptez réinstaller).',
       mbConfirmation, MB_YESNO or MB_DEFBUTTON2) = IDYES);
    if Supprimer then
      DelTree(Racine, True, True, True)
    else if FileExists(Cfg) then
      FileCopy(Cfg, Racine + '\config.properties.sauvegarde', False);
  end;
end;