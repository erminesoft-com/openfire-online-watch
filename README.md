Online Watch Plugin Overview

The Online Watch Plugin is a service that add/remove information about users online status into Redis.

Installation
Copy online_watch.jar into the plugins directory of your Openfire installation. The
    plugin will then be automatically deployed.

Using the Plugin
After plugin was installed first time it use default settings (url - "localhost", port - "6379", namespace -  "ersto_users_online_").
Admin can change that settings but plugin has to be reloaded manually.
Custom plugin settings could be found here server -> Server Settings -> Online watch properties.


General workflow
The main plugin goal is to register when user become online. When user connect to Openfire, Openfire creates new session for him when and in this moment new record in Redis created. When session is closing the information about that user's status deleted from Redis.
To speed up time for search user's status in Redis key for every user is builded from two parts - basic parts and first symbol of user name, see below "Key creation for Redis". For example four users with names : ryangosling, bradpitt, ryanreynolds and emmastone enter to Openfire and new new sessions were created.

Keys:
1) ryangosling - "ersto_users_online_r"
2) BradPitt - "ersto_users_online_b"
3) RyanReynolds - "ersto_users_online_r"
4) EmmaStone - "ersto_users_online_e"

 As you can see two users have the same key, in this case under one key two different records will be created in Redis.
 
 Key -> ersto_users_online_r
 record "ryangosling" - "1502699481463"
 record "ryanreynolds" - "1502699481463"
 Key -> ersto_users_online_b
 record "bradpitt" - "1502699481463"
 Key -> ersto_users_online_e
 record "emmastone" - "1502699481463"
 
 Also when plugin starts in first time then it search for all online users and save information abouts their status to Redis. When plugin is removing from Openfire than all information that were saved to Redis will be deleted.
 
 Key creation for Redis
 Key creates from two parts: basic part is namespace ("ersto_users_online_") and first symbol of username, e.g. basic part is "ersto_users_online_" and username is "newOpenfireUser" than key will be - "ersto_users_online_n".
 
 Datastructure in Redis to store users' statuses
 To store information about users statuses <b>Hash</b> is used. 
 For example user with name "newopenfireuser" enter to Openfire, new session creaets for that user, then plugin creates key  "ersto_users_online_n". Ander that key information about user"s session creation timestamp saved in hash: 
 key - "newopenfireuser" 
 timestamp - "1502699481463"
 
