# ADR-0003 : Tests d'intégration sur PostgreSQL local

- **Statut** : Accepted
- **Date** : 2026-10-18

## Contexte

Les tests d'intégration doivent s'exécuter sur le même moteur que la production
(PostgreSQL). La machine de développement dispose de 8 Go de RAM et Docker la ralentit
fortement : Testcontainers n'est donc pas une option locale.

## Options envisagées

1. H2 en mémoire.
2. Testcontainers (PostgreSQL dans un conteneur).
3. Base PostgreSQL de test dédiée, installée localement.

## Décision

Option 3. Une base `spendwise_test`, distincte de `spendwise_dev`, est utilisée par le
profil `test`. Flyway y applique les migrations au démarrage des tests. En intégration
continue (GitHub Actions), PostgreSQL est fourni comme service du workflow.

H2 est écarté : son dialecte diffère de PostgreSQL et masque des erreurs (types, SQL
spécifique, contraintes).

## Conséquences

- Positives : fidélité maximale à la production ; aucun Docker requis.
- Négatives : PostgreSQL doit être installé et la base créée avant de tester ; l'état
  de la base est partagé, donc chaque test doit être indépendant et nettoyer ses
  données ; les tests ne sont pas hermétiques comme avec Testcontainers.
- À réévaluer si l'environnement de développement change (ADR de remplacement).
