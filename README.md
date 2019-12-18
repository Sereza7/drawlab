# drawlab
ILIHM_C project


This project is the prototype of an interface for collaborative drawing in the context of the application Drawlab.

How to launch:

Setup JRE 1.8 (tested with Java SE 1.8.0_191).
Launch one server by executing the main of the class "src.serveur.MainGlobalServeur".

Launch two clients by executing twice the main of the class "src.main.MainClient".

How to use:
An account can only be used by one person at a time. This is a prototype, so when  closing an application, the user won't get log out.
The password field for authentication is useless (and no password is saved anywhere), because we didn't take time to properly think about how to handle a proper authentication system.
Only "parent" accounts can create a game-session.

For now, no matter the session, the game will be launched and the editors updated.
The game of a child (joiner) will be launched only when the creator of the game presses its button "Start".
(we intented the children to be able to join a game session mid way)
