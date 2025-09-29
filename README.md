# Ourcq'Spot

L'application mobile pour vos aventures sur le Canal de l'Ourcq !

## Accès git

🔐 Configuration de l’authentification
---------------------------------------

=> Configurer SSH

1. Vérifiez si vous avez déjà une clé :
ls ~/.ssh/id_rsa.pub

2. Sinon, créez-en une :
ssh-keygen -t rsa -b 4096 -C "votre-email@exemple.com"

Appuyez sur ENTRÉE à chaque question pour les valeurs par défaut.

3. Ajoutez votre clé SSH à GitHub :
- Copiez la clé :
cat ~/.ssh/id_rsa.pub

- Collez-la ici : https://github.com/settings/keys

4. Testez la connexion :
ssh -T git@github.com

📤 Travailler avec Git
-----------------------

1. Cloner le répertoire Github :
git clone git@github.com:Ourcq-Spot/OurcqSpot.git

2. Allez dans le dossier cloné :
cd OurcqSpot

3. Vérifiez la branche courante :
git branch

4. Utilisez git pull / add / commit / push pour interagir avec ce dépôt.

______________________

## Accès git

🔐 Configuration de l’authentification
---------------------------------------

=> Configurer SSH

1. Vérifiez si vous avez déjà une clé :
ls ~/.ssh/id_rsa.pub

2. Sinon, créez-en une :
ssh-keygen -t rsa -b 4096 -C "votre-email@exemple.com"

Appuyez sur ENTRÉE à chaque question pour les valeurs par défaut.

3. Ajoutez votre clé SSH à GitHub :
- Copiez la clé :
cat ~/.ssh/id_rsa.pub

- Collez-la ici : https://github.com/settings/keys

4. Testez la connexion :
ssh -T git@github.com

📤 Travailler avec Git
-----------------------

1. Cloner le répertoire Github :
git clone git@github.com:Ourcq-Spot/OurcqSpot.git

2. Allez dans le dossier cloné :
cd OurcqSpot

3. Vérifiez la branche courante :
git branch

4. Utilisez git pull / add / commit / push pour interagir avec ce dépôt.

______________________

## Dépendances (à installer)

- Android Studio

## Ouvrir le projet

Dans Android Studio, ouvrir le répertoire du projet "OurcqSpot".
Les dépendances se trouvent dans build.gradle.kts, la configuration de l'app Android dans app/manifests/AndroidManifest.xml, et le code source dans app/kotlin+java/com.ourcqspot.client.

## Maintenance

...
