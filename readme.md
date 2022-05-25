# Alterlite
<a href='https://play.google.com/store/apps/details?id=com.enxy.noolite&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png' height=65px/></a> &nbsp;&nbsp;  
&nbsp;&nbsp;&nbsp;![GitHub](https://img.shields.io/github/license/enxy0/Noolite?style=for-the-badge)

Alterlite - Android-приложение для управления освещением системы умного дома nooLite ethernet-шлюза PR1132. Является альтернативным клиентом официальному приложению [nooLite](https://play.google.com/store/apps/details?id=com.noolite) от производителя.

## Скриншоты
| <img src="https://raw.githubusercontent.com/enxy0/Noolite/master/github/images/home.png?raw=true"/>  | <img  src="https://raw.githubusercontent.com/enxy0/Noolite/master/github/images/details.png?raw=true" /> |  <img  src="https://raw.githubusercontent.com/enxy0/Noolite/master/github/images/script.png?raw=true"/> |  <img  src="https://raw.githubusercontent.com/enxy0/Noolite/master/github/images/settings.png?raw=true"/> |
|-|-|-|-|

## Возможности
* Управление освещением:
  * Включение / выключение света
  * Смена яркости
  * Смена цвета
  * Включение / выключение режима переливания цвета
* Создание пользовательских сценариев
* Добавление группы в избранное
* Поддержка темной темы приложения


## Использованные библиотеки
* [Jetpack Compose](https://developer.android.com/jetpack/compose) (1.2.0-beta02) - is Android's modern toolkit for building native UI.
* [Detekt](https://github.com/detekt/detekt) (1.20.0) - a static code analysis tool for the Kotlin programming language.
* [Koin](https://insert-koin.io/) (3.2.0) - A pragmatic lightweight dependency injection framework for Kotlin developers.
* [Android KTX](https://developer.android.com/kotlin/ktx) (1.7.0) - is a set of Kotlin extensions that are included with Android Jetpack and other Android libraries.
* [Material Components](https://github.com/material-components/material-components-android) (1.5.0) - help developers execute Material Design.
* [Jetpack Navigation](https://developer.android.com/guide/navigation) (2.4.1) - helps you implement navigation, from simple button clicks to more complex patterns, such as app bars and the navigation drawer.
* [OkHttp](https://square.github.io/okhttp/) (4.9.3) - is an efficient HTTP & HTTP/2 client.
* [Retrofit](https://square.github.io/retrofit/) (2.9.0) - is the class through which your API interfaces are turned into callable objects.
* [Room](https://developer.android.com/training/data-storage/room) (2.4.2) - provides an abstraction layer over SQLite to allow fluent database access while harnessing the full power of SQLite.
* [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization) (1.3.3) - consists of a compiler plugin, that generates visitor code for serializable classes, runtime library with core serialization API and support libraries with various serialization formats.
* [Timber](https://github.com/JakeWharton/timber) (5.0.1) - is a logger with a small, extensible API which provides utility on top of Android's normal Log class.

## Noolite PR1132 API (основные команды)
| Аргумент | Описание | Значение | Действие |  
|--|--|--|--|  
| ch | Адрес канала | 0..31 | Адрес канала, к которому будет применяться какая-либо команда.. |  
| cmd | Команда | 0 | Выключить (нагрузку) |  
| cmd | Команда | 1 | Запустить плавное понижение яркости|  
| cmd | Команда | 2 | Включить (нагрузку) |  
| cmd | Команда | 3 | Запустить плавное повышение яркости |  
| cmd | Команда | 4 | Включить или выключить нагрузку (переключатель состояния)|  
| cmd | Команда | 5 | Запустить плавное изменение яркости в обратном направлении  
| cmd | Команда | 7 | Запустить записанный сценарий|  
| cmd | Команда | 8 | Записать сценарий|  
| cmd | Команда | 9 | Отвязать выбранный канал от сервера|  
| cmd | Команда | 10 | Остановить регулировку |  
| cmd | Команда | 15 | Привязать выбранный канал к серверу|  
| cmd | Команда | 16 | Включить плавное переливание цвета (выключается командой cmd=10)|  
| cmd | Команда | 17 | Сменить цвет |  
| cmd | Команда | 18 | Сменить режим работы|  
| cmd | Команда | 19 | Сменить скорость эффекта в режиме работы|  
| br | Яркость | 0..100 (в %) | Абсолютная яркость. Использовать ее только с командой cmd=6. Чтобы изменить яркость на выбранном канале используйте следующие аргументы: ch, cmd и br (`/api.htm?ch=0&cmd=6&br=50`).<br>**Примечание автора**: при смене подсветки канала первого типа (type=1), нестабильно работает изменение яркости `/api.htm?ch=0&cmd=6&br=50`, поэтому рекомендую использовать данное сочетание аргументов: `/api.htm?ch=0&cmd=6&fm=3&br=50` (взято из ориг. приложения Noolite)|  
| fm | Формат | 0..255 | При передаче команды cmd=6 - значение fm=1 (яркость – байт данных 0) или fm=3 (яркость на каждый канал независимо - байт данных 0, 1, 2).<br>Аргумент «fm» необходим только при передаче данных вместе с аргументами («d0», «d1»,«d2»,«d3»). |

**Примеры:**

1. `http://192.168.0.168/api.htm?ch=0&cmd=15` (Привязать канал №1 к серверу)
2. `http://192.168.0.168/api.htm?ch=2&cmd=2` (Включить нагрузку на канале №3)
3. `http://192.168.0.168/api.htm?ch=2&cmd=0` (Выключить нагрузку на канале №3)
4. `http://192.168.0.168/api.htm?ch=2&cmd=4` (Изменить состояние нагрузки на канале №3)
5. `http://192.168.0.168/api.htm?ch=2&cmd=6&fm=3&br=50` (Установить яркость подсветки в 50% на канале №3)

Полную **документацию** по ethernet-шлюзу PR1132 **можно найти тут**: [PR1132.pdf](https://github.com/enxy0/Noolite/blob/master/github/PR1132.pdf)