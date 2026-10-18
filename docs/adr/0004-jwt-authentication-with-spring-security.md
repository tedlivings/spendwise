# ADR-0004 : Authentification JWT avec Spring Security

- **Statut** : Accepted
- **Date** : 2026-10-18

## Contexte

L'API REST est consommée par un frontend Angular. Elle doit être sans état (_stateless_)
et chaque utilisateur ne doit accéder qu'à ses propres données.

## Options envisagées

1. Sessions serveur avec cookie (`JSESSIONID`).
2. JWT géré avec une bibliothèque tierce (par exemple jjwt).
3. JWT géré par Spring Security OAuth2 Resource Server.
4. Serveur d'identité externe (Keycloak).

## Décision

Option 3. Le module `identity` émet les jetons ; Spring Security Resource Server les
valide à chaque requête. Le support JWT est déjà intégré à Spring Security : moins de
dépendances et moins de code de sécurité écrit à la main.

Le choix de l'algorithme de signature, la durée de vie des jetons et la gestion des
jetons de rafraîchissement seront tranchés dans un ADR dédié au démarrage du module
`identity`.

## Conséquences

- Positives : API sans état ; sécurité maintenue par le framework ; peu de dépendances.
- Négatives : un JWT émis ne se révoque pas simplement avant son expiration, d'où des
  durées de vie courtes ; la complexité d'un serveur d'identité externe est reportée,
  pas supprimée.
