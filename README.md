# PlayBattlegrounds
[![CircleCI](https://circleci.com/gh/voghDev/PlayBattlegrounds/tree/master.svg?style=svg)](https://circleci.com/gh/voghDev/PlayBattlegrounds/tree/master)

Sample project to do some requests to the PUBG Open API

<img height="100" src="./img/kotlin.png" width="100"> <img height="100" src="https://avatars2.githubusercontent.com/u/29458023?v=4&amp;s=200" width="100">

This is a small App that tries to work on the following aspects:

- Dependency Injection in Kotlin using [Kodein 5.x][1]
- Basic usage of Monads such as *Either* or *Try*, using [arrow-kt][2]
- a testable Kotlin architecture where requirements can be covered with Unit Tests. [You can see some examples][12]
- Kotlin Code style & formatting check using [ktlint][3]
- Android UI Design using [ConstraintLayout][5]
- Elegant & comfortable *RecyclerView* management using [Renderers 3.x][11]
- A small Database layer using [DBFlow][10], fully implemented in Kotlin
- Continous Integration using [CircleCI][8] - thanks to [JcMinarro][6] for [this awesome PR][9]

![Sample][appSample]

Installing
----------

Clone this repository and create a file named `pubg-api.properties`. Paste your PUBG ApiKey inside it like this:

    apiKey=abc12345

where `abc12345` is your PUBG ApiKey. You can see an example in [pubg-api-sample.properties][7]

Icons
-----

All icons, such as the [Chicken dinner](https://www.flaticon.com/free-icon/roast-chicken_889702#term=chicken&page=1&position=36) or the ["Load more" icon](https://www.flaticon.com/free-icon/round-add-button_61733#term=more&page=1&position=19) were downloaded from [flaticon.com][4]

Supporting sites
----------------

Thanks to [sokogames][13] for their contribution to support this project

[1]: https://github.com/Kodein-Framework/Kodein-DI/
[2]: https://github.com/arrow-kt/arrow
[3]: https://github.com/shyiko/ktlint
[4]: http://www.flaticon.com
[5]: https://developer.android.com/reference/android/support/constraint/ConstraintLayout
[6]: https://github.com/JcMinarro
[7]: https://github.com/voghDev/PlayBattlegrounds/blob/master/pubg-api-sample.properties
[8]: https://circleci.com/
[9]: https://github.com/voghDev/PlayBattlegrounds/pull/9
[10]: https://github.com/Raizlabs/DBFlow
[11]: https://github.com/pedrovgs/Renderers
[12]: https://github.com/voghDev/PlayBattlegrounds/blob/master/app/src/test/kotlin/es/voghdev/playbattlegrounds/features/players/ui/presenter/PlayerSearchPresenterTest.kt
[13]: http://www.sokogames.com

[appSample]: ./img/sample.gif
