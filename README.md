# 🔥 ESC/POS Thermal Printer Library for Kotlin Multiplatform (KMP) 

Cette bibliothèque Kotlin Multiplatform (KMP) permet l'impression sur des imprimantes thermiques ESC/POS via Bluetooth, TCP et USB. Elle est compatible avec les projets Android et Desktop, et peut être étendue à d'autres plateformes supportées par KMP.

⭐ **Star ce dépôt** pour soutenir ce projet et augmenter sa visibilité auprès des développeurs ayant besoin d'une solution multiplateforme pour les imprimantes thermiques ESC/POS ! 😊

## ✨ Fonctionnalités

- **Support multiplateforme** : Fonctionne sur Android, Ios et Desktop.
- **Compatibilité ESC/POS** : Supporte les commandes ESC/POS pour communiquer avec les imprimantes thermiques.
- **Options de connectivité multiples** : Connexion via Bluetooth, TCP (Ktot network socket) ou BT (bientôt disponible).
- **Intégration facile** : Facile à intégrer dans les projets KMP avec une API unifiée pour toutes les plateformes.
- **Personnalisable** : Possibilité d'étendre et de modifier les capacités d'impression avec des commandes ESC/POS personnalisées.

## 🚀 Démarrage

### Installation

Pour ajouter cette bibliothèque à votre projet, commencez par ajouter les dépendances dans votre fichier `build.gradle.kts` ou `build.gradle`.

Incluez les dépendances suivantes dans le source set `commonMain` :

`io.github.mtlabdo:escprinterlib:$version`

```kotlin
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.example:escpos-library:$version")
            }
        }
    }
}
```

## Tested printers

- xprinter thermal receipt printer 80MM



## Test it !
To test this library, it's pretty simple !

- Create a directory and open a terminal inside
- Run git clone https://github.com/mtlabdo/PrinterKMP.git
- Open the directory with Android Studio

