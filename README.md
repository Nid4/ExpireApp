ExpireApp

ExpireApp to aplikacja Android służąca do zarządzania produktami sklepowymi.
🎯 Cel

Celem aplikacji jest ułatwienie zarządzania produktami poprzez lokalną bazę danych zsynchronizowaną z chmurą.
🛠️ Problem, który rozwiązuje

    Czasochłonna inwentaryzacja: Tradycyjna metoda inwentaryzacji jest czasochłonna i podatna na błędy.

    Brak synchronizacji: Użytkownicy nie mają synchronizacji między urządzeniami, co utrudnia pracę.

    Potrzeba szybkiego dodawania produktów: Użytkownicy potrzebują szybkiego sposobu na dodawanie produktów, np. przez skanowanie kodów kreskowych.

🚀 Główne funkcjonalności aplikacji

    ✔ Logowanie użytkowników (Firebase Authentication)

    ✔ Zarządzanie produktami w lokalnej bazie Room, synchronizacja z Firebase

    ✔ Podział na dni – lista produktów dla danego dnia

    ✔ Skanowanie kodów kreskowych (ML Kit)

    ✔ Przejrzysty interfejs (RecyclerView, DatePicker)

⚡ Wymagania

Aby uruchomić aplikację na swoim urządzeniu, musisz spełnić następujące wymagania:

    Android Studio (lub inne IDE do pracy z Androidem)

    Android 5.0 (API 21) lub wyższy

    Google Firebase (dla integracji z Google API)

⚡ Konfiguracja
1. Klonowanie repozytorium

Sklonuj repozytorium na swoje urządzenie:

git clone https://github.com/Nid4/ExpireApp.git
cd ExpireApp

2. Dodanie pliku konfiguracyjnego Google API

Aby aplikacja mogła korzystać z Firebase i innych usług Google, musisz dodać swój własny plik konfiguracyjny google-services.json.
Instrukcje:

    Zaloguj się na swoje konto w Firebase Console.

    Utwórz nowy projekt, jeśli go jeszcze nie masz.

    Przejdź do zakładki "Ustawienia projektu" i pobierz plik google-services.json.

    Umieść ten plik w katalogu app/ w swoim projekcie Android Studio (zastępując istniejący plik, jeśli już go masz).

3. Gradle Configuration

Upewnij się, że Twoje pliki Gradle są prawidłowo skonfigurowane, aby korzystać z Firebase:

    W pliku build.gradle (Project) dodaj:

classpath 'com.google.gms:google-services:4.3.10'

    W pliku build.gradle (App) dodaj:

apply plugin: 'com.google.gms.google-services'

4. Synchronizacja i uruchomienie

    Zsynchronizuj projekt z plikami Gradle (naciśnij "Sync Now" w Android Studio).

    Uruchom aplikację na swoim urządzeniu lub emulatorze.

⚠️ Ważne uwagi

    google-services.json: Upewnij się, że Twój plik google-services.json znajduje się w katalogu app/. Bez niego aplikacja nie będzie mogła łączyć się z usługami Firebase.

    Zabezpieczenie API Keys: Pamiętaj, aby nie umieszczać plików zawierających klucze API w repozytorium. Zawsze dodawaj je do .gitignore!

🛠️ Technologie

    Język programowania: Kotlin

    Środowisko: Android Studio

    Biblioteki i technologie:

        ✅ Firebase Authentication – logowanie użytkowników

        ✅ Room (Jetpack) – lokalna baza danych

        ✅ Firebase Firestore – synchronizacja danych w chmurze

        ✅ RecyclerView – dynamiczna lista produktów i dni

        ✅ ML Kit (Barcode Scanner) – skanowanie kodów kreskowych

        ✅ DatePicker – wybór daty ważności produktów
