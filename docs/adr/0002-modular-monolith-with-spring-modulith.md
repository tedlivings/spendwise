# ADR-0002 : Monolithe modulaire avec Spring Modulith

- **Statut** : Accepted
- **Date** : 2026-10-18

## Contexte

L'application comporte des domaines distincts (identité, catégories, dépenses, budgets,
rapports, notifications). Il faut des frontières nettes entre eux sans payer d'emblée
la complexité de la distribution.

## Options envisagées

1. Monolithe en couches classiques (`controller`, `service`, `repository` globaux).
2. Microservices.
3. Monolithe modulaire avec Spring Modulith.

## Décision

Option 3. Chaque domaine est un **module** : un sous-package direct du package racine
`io.github.tedlivings.spendwise`. Seul le contenu de la racine d'un module est son API
publique ; le reste est interne. Les modules communiquent par appels à des API
publiques ou par événements applicatifs. Un test `ApplicationModules.verify()` fait
échouer le build si une frontière est violée.

## Conséquences

- Positives : frontières vérifiées automatiquement ; un seul déploiement, une seule
  base ; chaque module pourra devenir un service indépendant si le besoin se présente.
- Négatives : la discipline repose sur les tests, car la JVM reste commune ; les
  transactions et la base sont partagées, il faudra éviter les jointures entre
  tables de modules différents.
