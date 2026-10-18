# ADR-0001 : Consigner les décisions d'architecture

- **Statut** : Accepted
- **Date** : 2026-10-18

## Contexte

SpendWise est un projet d'apprentissage qui doit refléter les pratiques professionnelles.
Les choix d'architecture sont nombreux et leur justification s'oublie vite.

## Options envisagées

1. Ne rien documenter et se fier à la mémoire.
2. Un wiki externe au dépôt.
3. Des ADR au format MADR, versionnés dans le dépôt.

## Décision

Option 3. Les ADR sont écrits en Markdown dans `docs/adr/`, numérotés sur quatre
chiffres, et relus via Pull Request comme du code.

## Conséquences

- Positives : l'historique des décisions suit celui du code ; la relecture est gratuite.
- Négatives : une discipline d'écriture est nécessaire à chaque décision structurante.
