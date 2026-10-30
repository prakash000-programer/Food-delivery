@echo off
REM ============================================================
REM Food-delivery project scaffolder
REM Run this from the folder where you want "Food-delivery" to
REM be created (e.g. your Desktop or Projects folder).
REM Safe to re-run: existing folders/files are left untouched.
REM ============================================================

set ROOT=Food-delivery
set BASE=%ROOT%\src\com\fooddelivery

REM --- Create package folders ---
if not exist "%BASE%\cli"        mkdir "%BASE%\cli"
if not exist "%BASE%\enums"      mkdir "%BASE%\enums"
if not exist "%BASE%\models"     mkdir "%BASE%\models"
if not exist "%BASE%\repository" mkdir "%BASE%\repository"
if not exist "%BASE%\services"   mkdir "%BASE%\services"

REM --- Main entry point (won't overwrite if it already exists) ---
if not exist "%BASE%\Main.java" type nul > "%BASE%\Main.java"

REM --- One file per entity in each of the 4 layers ---
for %%E in (User Restaurant MenuItem Cart Order OrderItem DeliveryPartner Payment Coupon Rating) do (
    if not exist "%BASE%\models\%%E.java"             type nul > "%BASE%\models\%%E.java"
    if not exist "%BASE%\cli\%%ECli.java"              type nul > "%BASE%\cli\%%ECli.java"
    if not exist "%BASE%\repository\%%ERepository.java" type nul > "%BASE%\repository\%%ERepository.java"
    if not exist "%BASE%\services\%%EService.java"      type nul > "%BASE%\services\%%EService.java"
)

echo.
echo Done. Created/verified folders + 1 Main.java + 40 entity files
echo (10 entities x 4 layers: models, cli, repository, services).
pause