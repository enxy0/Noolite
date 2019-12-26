# Noolite (PR1132) - Kotlin Android App

Noolite - Android приложение, являющееся альтернативным клиентом для Ethernet-шлюза PR1132 системы умного дома Noolite. В данном приложении я попытался сделать максимально удобный и приятный глазу интерфейс (чего как раз и не хватало в оригинальном приложении [Noolite](https://play.google.com/store/apps/details?id=com.noolite)).

**Что приложение умеет:**

* Показывать группы и каналы.
* Добавлять группу в избранное. При каждом запуске будет открываться выбранная вами группа.
* Включать и выключать свет (вкл/выкл нагрузку).
* Менять яркость подсветки.
* Менять цвет подсветки, включать и выключать переливание из одного цвета в другой.
* Подгружать все группы и каналы с сервера.
* Показывать ошибку, если телефон не подключен к WiFi.
* Менять темы интерфейса (White Blue, Dark Green and Black Amber)

**Что приложение не умеет:**
* Подгружать все группы и каналы с сервера, используя аутентификацию.
* Обновлять и отображать данные с сенсоров.
 
**Приложение написано на Kotlin при помощи:**
* Model-View-ViewModel (MVVM) architecture pattern
* [Retrofit](https://github.com/square/retrofit) (2.7.0)
* [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines) (1.3.3)
* [Kotlin Coroutines Adapter](https://github.com/JakeWharton/retrofit2-kotlin-coroutines-adapter) (0.9.2)
* [Dagger 2](https://github.com/google/dagger) (2.20)
* [Gson](https://github.com/google/gson) (2.8.6)
* [Android Architecture Components: ViewModel (1.0.0-rc03)](https://developer.android.com/topic/libraries/architecture/viewmodel)
* [Android KTX](https://developer.android.com/kotlin/ktx)

# Скриншоты
<img  src="/extras/screenshot_all_groups.png?raw=true"  width=33% /> <img  src="/extras/screenshot_chosen_room.png?raw=true"  width=33% /> <img  src="/extras/screenshot_settings.png?raw=true"  width=33% />

# Как использовать данный проект

Вы можете найти готовый APK файл во вкладке `Releases`.

Для автоматической загрузки списка групп при первом запуске приложения, перейдите в `app/src/main/java/com/enxy/noolite/core/platform/FileManager.kt` и измените стандартный IP адрес:

```kotlin
...
const val DEFAULT_IP_ADDRESS_VALUE = "ВАШ_IP_АДРЕС" // например "192.168.0.168"
...
```

**Также убедитесь, что ваша система Noolite PR1132 уже настроена через web-интерфейс (все группы с каналалами добавлены и работают)**

Если стандартный IP адрес вы не меняли, то тогда вам придется перейти во вкладку `Настройки` в приложении, внести изменения в поле Адрес и нажать кнопку `Обновить`.

# Noolite API (основные команды)
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
| br | Яркость | 0..100 (в %) | Абсолютная яркость. Использовать ее только с командой cmd=6. Чтобы изменить яркость на выбранном канале используйте следующие аргументы: ch, cmd и br (`/api.htm?ch=0&cmd=6&br=50`). **Примечание автора**: при смене подсветки канала первого типа (type=1), плохо работает изменение яркости `/api.htm?ch=0&cmd=6&br=50`, поэтому рекомендую использовать данное сочетание аргументов: `/api.htm?ch=0&cmd=6&fm=3&br=50`|
| fm | Формат | 0..255 | При передаче команды cmd=6 - значение fm=1 (яркость – байт данных 0) или fm=3 (яркость на каждый канал независимо - байт данных 0, 1, 2). Аргумент «fm» необходим только при передаче данных вместе с аргументами («d0», «d1»,«d2»,«d3»). |

**Примеры:**

`http://192.168.0.168/api.htm?ch=0&cmd=15` (Привязать канал №1 к серверу)

`http://192.168.0.168/api.htm?ch=2&cmd=2` (Включить нагрузку на канале №3)

`http://192.168.0.168/api.htm?ch=2&cmd=0` (Выключить нагрузку на канале №3)

`http://192.168.0.168/api.htm?ch=2&cmd=4` (Изменить состояние нагрузки на канале №3)

`http://192.168.0.168/api.htm?ch=2&cmd=6&fm=3&br=50` (Установить яркость подсветки в 50% на канале №3)

Полную и официальную документацию можно найти тут: [PR1132.pdf](/extras/PR1132.pdf)

# Иконки

Выражаю благодарность следующим авторам:

<img  src="https://image.flaticon.com/icons/svg/606/606795.svg"  width=30> Icon made by <a  href="https://www.flaticon.com/authors/good-ware"  title="Good Ware">Good Ware</a> from <a  href="https://www.flaticon.com/"  title="Flaticon">www.flaticon.com</a> is licensed by <a  href="http://creativecommons.org/licenses/by/3.0/"  title="Creative Commons BY 3.0"  target="_blank">CC 3.0 BY</a>

<img  src="https://image.flaticon.com/icons/svg/865/865140.svg"  width=30> Icon made by <a  href="https://www.flaticon.com/authors/good-ware"  title="Good Ware">Good Ware</a> from <a  href="https://www.flaticon.com/"  title="Flaticon">www.flaticon.com</a> is licensed by <a  href="http://creativecommons.org/licenses/by/3.0/"  title="Creative Commons BY 3.0"  target="_blank">CC 3.0 BY</a>

<img  src="https://image.flaticon.com/icons/svg/339/339853.svg"  width=30> Icon made by <a  href="https://www.flaticon.com/authors/cursor-creative"  title="Cursor Creative">Cursor Creative</a> from <a  href="https://www.flaticon.com/"  title="Flaticon">www.flaticon.com</a> is licensed by <a  href="http://creativecommons.org/licenses/by/3.0/"  title="Creative Commons BY 3.0"  target="_blank">CC 3.0 BY</a>

<img  src="https://image.flaticon.com/icons/svg/446/446108.svg"  width=30> Icon made by <a  href="https://www.flaticon.com/authors/freepik"  title="Freepik">Freepik</a> from <a  href="https://www.flaticon.com/"  title="Flaticon">www.flaticon.com</a> is licensed by <a  href="http://creativecommons.org/licenses/by/3.0/"  title="Creative Commons BY 3.0"  target="_blank">CC 3.0 BY</a>

<img  src="https://image.flaticon.com/icons/svg/863/863684.svg"  width=30> Icon made by <a  href="https://www.flaticon.com/authors/good-ware"  title="Good Ware">Good Ware</a> from <a  href="https://www.flaticon.com/"  title="Flaticon">www.flaticon.com</a> is licensed by <a  href="http://creativecommons.org/licenses/by/3.0/"  title="Creative Commons BY 3.0"  target="_blank">CC 3.0 BY</a>

<img  src="https://image.flaticon.com/icons/svg/126/126479.svg"  width=30> Icon made by <a  href="https://www.flaticon.com/authors/gregor-cresnar"  title="Gregor Cresnar">Gregor Cresnar</a> from <a  href="https://www.flaticon.com/"  title="Flaticon">www.flaticon.com</a> is licensed by <a  href="http://creativecommons.org/licenses/by/3.0/"  title="Creative Commons BY 3.0"  target="_blank">CC 3.0 BY</a>

<img  src="https://image.flaticon.com/icons/svg/1077/1077035.svg"  width=30> Icon made by <a  href="https://www.flaticon.com/authors/freepik"  title="Freepik">Freepik</a> from <a  href="https://www.flaticon.com/"  title="Flaticon">www.flaticon.com</a> is licensed by <a  href="http://creativecommons.org/licenses/by/3.0/"  title="Creative Commons BY 3.0"  target="_blank">CC 3.0 BY</a>

<img  src="https://image.flaticon.com/icons/svg/159/159841.svg"  width=30> Icon made by <a  href="https://www.flaticon.com/authors/gregor-cresnar"  title="Gregor Cresnar">Gregor Cresnar</a> from <a  href="https://www.flaticon.com/"  title="Flaticon">www.flaticon.com</a> is licensed by <a  href="http://creativecommons.org/licenses/by/3.0/"  title="Creative Commons BY 3.0"  target="_blank">CC 3.0 BY</a>

<img  src="https://image.flaticon.com/icons/svg/1451/1451553.svg"  width=30> Icon made by <a  href="https://www.flaticon.com/authors/freepik"  title="Freepik">Freepik</a> from <a  href="https://www.flaticon.com/"  title="Flaticon">www.flaticon.com</a> is licensed by <a  href="http://creativecommons.org/licenses/by/3.0/"  title="Creative Commons BY 3.0"  target="_blank">CC 3.0 BY</a>

<img  src="https://image.flaticon.com/icons/svg/126/126472.svg"  width=30> Icon made by <a  href="https://www.flaticon.com/authors/gregor-cresnar"  title="Gregor Cresnar">Gregor Cresnar</a> from <a  href="https://www.flaticon.com/"  title="Flaticon">www.flaticon.com</a> is licensed by <a  href="http://creativecommons.org/licenses/by/3.0/"  title="Creative Commons BY 3.0"  target="_blank">CC 3.0 BY</a>

<img  src="https://image.flaticon.com/icons/png/512/0/375.png"  width=30> Icon made by <a href="https://www.flaticon.com/authors/freepik" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a>

<img  src="https://image.flaticon.com/icons/png/512/159/159599.png"  width=30> Icon made by <a href="https://www.flaticon.com/authors/gregor-cresnar" title="Gregor Cresnar">Gregor Cresnar</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a>

<img  src="https://image.flaticon.com/icons/png/512/112/112489.png"  width=30> Icon made by <a href="https://www.flaticon.com/authors/freepik" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a>