# Arista
Arista est une application Android destinée à suivre les données personnelles de l'utilisateur telles que les exercices physiques et les cycles de sommeil.

## Fonctionnalités
- Enregistrement des exercices physiques de l'utilisateur : durée, intensité, catégorie.
- Suivi du sommeil : heure de début, durée, qualité du sommeil.
- Profil utilisateur : informations personnelles comme le nom, l'email, le mot de passe.

## Pré-requis
- Android Studio
- SDK Android 

## Installation
- Clonez ce dépôt dans votre environnement local.
- Ouvrez le projet avec Android Studio.
- Lancez l'émulateur ou connectez votre appareil Android.
- Compilez et exécutez l'application.

## Utilisation
- Profil utilisateur : Enregistrez vos données personnelles.
- Exercices : Ajoutez vos exercices en cliquant sur le bouton "Ajouter" et en remplissant les informations nécessaires.
- Sommeil : Ajoutez vos cycles de sommeil en indiquant l'heure de début, la durée et la qualité de votre sommeil.

<p align="center">
  <img src="https://user.oc-static.com/upload/2025/02/18/1739888335313_Capture_d_e%CC%81cran_2025-02-18_a%CC%80_16.17.46-removebg-preview.png" alt="ARISTA logo" width="150"/>
</p>

# 💡 Projet Android - ARISTA  📱🧠

> 🚀 Continuation du **Projet 5** dans le cadre de notre mission avec la startup fictive **ARISTA** !

---

## 🌟 Objectif

🎯 Dans cet exercice, votre mission est de **persister les données** à l'aide de la librairie **Room** dans une application Android comportant **trois fragments spécifiques** intégrés dans une activité.

---

## 🧱 Architecture prévue

L'application se base sur votre diagramme de classes précédent. Vous devez y intégrer **Room** afin de garantir la persistance des données, même après une **rotation d’écran** ou un **redémarrage de l'application**.

---

## 🧩 Les 3 Fragments

| Fragment | Données | Modification possible ? |
|---------|---------|--------------------------|
| 👤 Utilisateur | Préremplies à la création de la base | ❌ Aucune modification |
| 🏋️ Exercices | Création via un FAB ➕ / Suppression via l'interface ❌ | ✅ Ajout / ✅ Suppression |
| 😴 Sommeil | Préremplies à la création de la base | ❌ Aucune modification |

---

## 🎨 Ce qui est déjà prêt

✅ **UI/UX** : la conception graphique de l'application est **déjà en place** !

🎨 Vous n'avez plus qu'à **connecter les données** grâce à Room.

---

## 🛠️ Ce que vous devez faire

1. 🔄 Reprendre votre **diagramme de classes**.
2. 🧠 Ajouter **Room** à votre projet :
   - Créer les entités, DAO, et base de données.
   - Gérer la création de la base avec pré-remplissage.
3. 📲 Gérer la persistance dans vos **ViewModel**.
4. ➕ Ajouter un **Floating Action Button (FAB)** pour créer des exercices.
5. ❌ Gérer la suppression d’un exercice via l’interface dédiée.

> 🎨 Vous avez **carte blanche** pour la structure et le style. L’objectif est de **rendre les données persistantes avec Room**. Faites-vous plaisir !

---

## 📚 Technologies utilisées

- 🟦 Kotlin
- 🏗️ MVVM
- 🏠 Room
- 📦 LiveData / StateFlow
- ✨ Material Components


## ✅ Exemple de Base de Données
<p align="center">
  <img src="https://user.oc-static.com/upload/2023/12/06/17018748594551_image3.png" alt="Tables" width="500"/>
</p>

---

## 🤖 USE CASE
<p align="center">
  <img src="https://miro.medium.com/v2/resize:fit:4800/format:webp/0*5eJUx2N-5IKoIJNO.png" alt="Tables" width="500"/>
</p>

---

## 📦 Exemple de structure Room

```kotlin
@Entity
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val duration: Int // en minutes
)

@Dao
interface ExerciseDao {
    @Insert fun insert(exercise: Exercise)
    @Delete fun delete(exercise: Exercise)
    @Query("SELECT * FROM Exercise") fun getAll(): LiveData<List<Exercise>>
}

