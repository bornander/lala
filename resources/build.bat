@echo off
set LOCAL=%~dp0

cd %LOCAL%
echo Clean
rmdir /S /Q ..\android\assets 

echo Build 
call C:\Dev\libGDX\texture_packer.bat %LOCAL%\raw\graphics\hud %LOCAL%\assets\graphics hud
call C:\Dev\libGDX\texture_packer.bat %LOCAL%\raw\graphics\blocks %LOCAL%\assets\graphics blocks
call C:\Dev\libGDX\texture_packer.bat %LOCAL%\raw\graphics\characters %LOCAL%\assets\graphics characters
call C:\Dev\libGDX\texture_packer.bat %LOCAL%\raw\graphics\tiles %LOCAL%\assets\graphics tiles
call C:\Dev\libGDX\texture_packer.bat %LOCAL%\raw\graphics\effects %LOCAL%\assets\graphics effects
call C:\Dev\libGDX\texture_packer.bat %LOCAL%\raw\graphics\menu %LOCAL%\assets\graphics menu
xcopy /S assets\*.* ..\android\assets\*.*
