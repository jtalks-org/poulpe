JTalks Poulpe
---
Poulpe is a part of [JTalks](http://jtalks.org) ecosystem of projects which represents admin panel for user management that is under active development.

* [Installation Guide](docs/installation/general-installation-guide.md) is to be read if you want just to try the app or you want to install it locally

For developers/QA/PMs/Leads:
* [How to join us](http://jtalks.org/display/jtalks/How+to+join+us)
* [Sonar](http://sonar.jtalks.org/dashboard/index/534)
* [Deployment Pipeline](http://ci.jtalks.org/view/Poulpe.%20Pipeline)
* [Stack of Technologies](http://jtalks.org/display/jtalks/Stack+of+technologies)
* [Code Conventions](http://jtalks.org/display/jtalks/Code+Conventions)

####State of Project
 - First the project was going to fully configure other components like JCommune, Antarcticle, but now we figured out that most of configuration should be placed inside of those components per se and instead Poulpe should manage only common stuff like Users, Groups.
 - Because of such change of directions, we have a lot of things that are moving to JCommune and should be removed from Poulpe.
 - We also have concerns on technical approach we've taken - we moved to MVVM model using ZK Framework which appeared to be a bad choice since it really clutters the classes, it's hard to test and read them. We probably want to move back to old MVP and thus some classes are going to stay as MVVM and some will follow MVP which might confuse people.
 - Right now we're working on features like OAuth and OpenID because users that register in JCommune should actually be registered in Poulpe and other components should then authenticate users via Poulpe.