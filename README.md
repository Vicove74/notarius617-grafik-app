# График 617 (Android)

Android приложение за вътрешния график на нотариус Красимир Бележков (notarius617.com).
Приложението отваря https://notarius617.com/grafik/?source=app в собствен прозорец. Графикът,
данните и входът са на сайта, така че промени по графика не изискват нова версия на приложението.

## Сваляне

Последна версия: https://github.com/Vicove74/notarius617-grafik-app/releases/latest/download/grafik-617.apk

На телефона: отваряте линка в Chrome, сваляте файла, отваряте го и разрешавате
„Инсталиране от неизвестни източници“ за Chrome, ако телефонът попита.

## Build

Всеки push в `main` пуска GitHub Actions (`.github/workflows/build-apk.yml`), който сглобява APK
и го публикува като нов Release. Подписът е с `app/signing.keystore`, за да може новите версии да се
инсталират върху старите.
