[Setup]
AppName=SmartCaisse
AppVersion=1.0
AppPublisher=Clever
DefaultDirName={autopf}\SmartCaisse
DefaultGroupName=SmartCaisse
OutputDir=C:\Users\Clever\Desktop\SmartCaisse\installer
OutputBaseFilename=SmartCaisse_Setup_v1.0
SetupIconFile=C:\Users\Clever\Desktop\SmartCaisse\icone.ico
Compression=lzma2
SolidCompression=yes
PrivilegesRequired=admin

[Languages]
Name: "french"; MessagesFile: "compiler:Languages\French.isl"

[Tasks]
Name: "desktopicon"; Description: "Créer un raccourci sur le Bureau"; GroupDescription: "Raccourcis"
Name: "startmenuicon"; Description: "Créer un raccourci dans le Menu Démarrer"; GroupDescription: "Raccourcis"

[Files]
Source: "C:\Users\Clever\Desktop\SmartCaisse\output\SmartCaisse\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs

[Icons]
Name: "{autodesktop}\SmartCaisse"; Filename: "{app}\SmartCaisse.exe"; IconFilename: "{app}\SmartCaisse.exe"; Tasks: desktopicon
Name: "{group}\SmartCaisse"; Filename: "{app}\SmartCaisse.exe"; Tasks: startmenuicon
Name: "{group}\Désinstaller SmartCaisse"; Filename: "{uninstallexe}"

[Run]
Filename: "{app}\SmartCaisse.exe"; Description: "Lancer SmartCaisse"; Flags: nowait postinstall skipifsilent