# Pokedex java boilerplate

# Introduction

Cet exercice consiste à implémenter un serveur back-end pour "presque" jouer à League of Legends.

Il s'agit d'un serveur REST dont chacun des endpoints est décrit dans les users stories ci-dessous.

Chaque user story rapporte un certain nombre de points. Si les tests automatisés associés à cette user story
fonctionnent,
ces points vous seront accordés.
Si une partie d'entres eux fonctionnent mais pas tous, vous marquerez des points au prorata des tests passés.
N'hésitez pas à implémenter vos propres tests unitaires !

Je serai particulièrement attentif à :

- Architecture de l'application
- Maintenabilité du code
- Gestion des exceptions
- Vos tests unitaires s'ils existent

Le boiler plate associé à ce README vous est fourni comme point de départ avec un endpoint `/api/status` qui ne doit pas
être modifié.
Des commentaires vous permettant de vous aider à récupèrer les données reçues avec le framework Javalin ont été placés
dans le fichier `App.java`.

# Description de l'application

Je souhaite simuler une partie de League Of Legends sur mon serveur java.
Pour cela, je souhaite indiquer pour une partie :
Les cinq champions, leur lane, leurs points de vie, et leurs compétences.

Une partie se passe sur trois chemins différents (lanes). Chaque chemin peut accueillir
n'importe quel nombre de champions, et les champions peuvent se déplacer de lane en lane, mais doivent
toujours passer par la JUNGLE avant de passer à une nouvelle lane.

Il y a deux équipe, chacune avec un nom (BLEU et ROUGE) et une équipe de 5 joueurs.

## Champion

Le nom du champion servira d'identifiant unique dans le cadre de cet exercice.
(attention donc aux petits tricheurs qui voudraient créer deux champions avec le même nom mais
avec une casse différente !)
Le nom doit respecter le contrat suivant : quelque soit le nom donnée à la création ou en recherche,
le nom dans ma base doit tout le temps commencer par une majuscule et le reste en minuscule.
(Peut-être qu'il existe un utils sur les chaines de caractères, qui sait...)

| Nom de l'attribut | Type           |
|-------------------|----------------|
| championName      | String         |
| championType      | Roles          |
| lifePoints        | int            |
| abilities         | List\<Ability> |

Les points de vie sont entre 100 et 150.

### Lanes

- TOP
- MID
- BOTTOM
- JUNGLE

## Type

Les champions existent forcément avec un role particulier :

- SUPPORT
- COMBATTANT
- MAGE
- TIREUR
- ASSASSIN
- AUTRE

## Ability

| Nom de l'attribut | Type   |
|-------------------|--------|
| abilityName       | String |
| damage            | int    |

# US 1.1 - Création d'un champion

En tant qu'utilisateur, je souhaite rajouter des champions à mon pool de champion.
j'envoie une requête de type POST contenant les informations nécessaires à sa création.

* Le nom (qui servira d'identifiant)
* Le role
* Le nombre de points de vie
* La liste des compétences

Si le champion existe déjà ou que le json est incomplet ou invalide, une erreur 400 est renvoyée par le serveur.
En cas de réussite, on renvoit un code 200.

Le endpoint à utiliser est `/api/create`

## Spécifications d'interfaces

### Requête

```json
{
  "championName": "Nasus",
  "role": "COMBATTANT",
  "lifePoints": 100,
  "abilities": [
    {
      "ability": "buveuse d'âme",
      "damage": 30
    }
  ]
}
```

# US 1.2 Modification d'un champion

Le endpoint à utiliser est `/api/modify`

En tant qu'utilisateur, je souhaite modifier un champion dans la base de données s'il existe.
J'envoie une requête JSON de type POST contenant le nom du champion et les informations à modifier.
Tous les attributs peuvent être modifiés, sauf le nom.
Pour la capacité, si la capacité existe déjà, elle n'est pas modifiée. On ne peut qu'ajouter de nouvelles
compétences, pas en mettre à jour.

Si la modification est effectuée, le serveur répond avec le code 200.
Si le champion n'existe pas, le serveur répond avec le code 404.
Si le json est invalide, le serveur répond avec une erreur 400.

## Spécifications d'interfaces

### Requête exemple 1

```json
{
  "championName": "Nasus",
  "lifePoints": 170
}
```

### Requête exemple 2

```json
{
  "championName": "Nasus",
  "abilities": [
    {
      "ability": "Vieillissement accéléré",
      "damage": 5
    }
}
```

# US 2 - Ajouter des champions à une équipe

En tant qu'utilisateur, je souhaite préparer une partie.
Pour cela, je souhaite indiquer pour chaque équipe la liste des champions qui en fera partie et la lane sur laquel 
ils seront placés.
J'envoie donc un json qui contient le nom de l'équipe que je veux remplir, et le nom des champions et leur placement.

Attention !
La taille de mon équipe ne peut pas dépasser 5 champions.
Pas de champions en double, que ce soit dans une équipe ou entre deux équipes.
(n'oubliez pas que je veux surveiller la casse)
Si je rentre dans un de ces cas, mon serveur doit me renvoyer une erreur 400.

Sinon, c'est un code 200.

Exemple de requête : `/api/team`


# US 3 - Lancer la partie

En tant qu'utilisateur, je souhaite "lancer une partie"
Ne plus autoriser de changements dans les équipes.
La partie commence si et seulement si j'ai deux équipes de 5 champions.

Si tout est bon, le serveur me renvoit un code 200 avec le message suivant : 
"Bienvenue sur la Faille de l'Invocateur !"

Exemple de requête : `/api/begin`

# US 4 - Rechercher les champions par lane

En tant qu'utilisateur, je souhaite récupérer la liste de champions qui sont sur la même lane.
J'envoie une requète de
type `GET` ayant pour paramètre le `laneType` contenant le type à rechercher.

Le serveur doit envoyer la liste des champions qui sont actuellement sur la lane demandée.

Si aucun champion n'est sur cette lane, une liste vide est renvoyée avec le code 200.

Si le type de lane recherché n'est pas dans la liste de type possible, 
le serveur renvoie une requête vide avec le code d'erreur 400.

Exemple de requête : `/api/searchByType?type=ELECTRIC`

## Spécifications d'interfaces

### Réponse

```json
{
  "result": [
    {
      "name": "Pikachu",
      "lifePoints": 80,
      "powers": [
        {
          "name": "gnaw",
          "damageType": "NEUTRAL",
          "damage": 30
        },
        {
          "name": "thunder jolt",
          "damageType": "ELECTRIC",
          "damage": 50
        }
      ]
    }
  ]
}
```

# US 4 - Calculer le vainqueur d'une lane

En tant qu'utilisateur, je souhaite savoir quelle équipe reporte quelle lane.
Pour cela, on calcule quelle équipe arrive à mettre l'autre équipe à 0 points de vie.

Exemple :
Nasus (180 pv, une compétence avec 30 d'attaque) combat pantheon (150 pv, une compétence avec 60 d'attaque)
Il faut 5 coups à Nasus pour tuer Panthéon, alors qu'il faut 3 coups à Panthéon pour tuer Nasus
Le vainqueur est ici Panthéon.
Pour commencer, on va dire qu'un champion utilisera toujours sa compétence la plus forte.
Si jamais il y a plus d'un champion sur la lane, il faut bien évidemment tous les prendre en compte.

Le endpoint à utiliser est `/api/prediction`

Attention, la prédiction ne peut se faire que lorsqu'une partie a commencé.

## Spécifications d'interfaces

### Réponse

```json
{
  "result": [
    {
      "laneName": "TOP",
      "winningTeam": "BLUE"
    },
    {
      "name": "JUNGLER",
      "winningTeam": "RED"
    },
    {
      "name": "MID",
      "winningTeam": "RED"
    },
    {
      "name": "BOT",
      "winningTeam": "NONE"
    }
  ]
}
```
