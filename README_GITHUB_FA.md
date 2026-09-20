# IRAPP Unified Final — GitHub Ready

نسخه: 3.3.3
تاریخ آماده‌سازی: 2026-09-17

## مبنا
این پروژه از `IRAPP_UNIFIED_FINAL_v3.3.2_BUTTONS_FIXED_A21S_GITHUB_READY` اصلاح شده است.
فایل `IRAPP-APK.zip` مبنا نیست.

## اصلاحات این نسخه
- دکمه‌های دسته‌بندی صفحه اصلی به صفحه دسته‌بندی واقعی وصل شدند.
- دسته‌های برنامه‌ها، بازی‌ها، ابزارها، امنیت و هوش مصنوعی مسیر قابل مشاهده دارند.
- دریافت و علاقه‌مندی در صفحه دسته‌بندی فعال است.
- جستجو در صفحه دسته‌بندی نیز نتایج همان دسته را به‌روزرسانی می‌کند.
- رویداد `mockFile` که در نسخه قبلی handler نداشت، به event handler اضافه شد.
- escape داده‌ها مقاوم‌تر شد.
- Android versionCode=306 و versionName=3.3.3.
- خودرویار، HAMED AI و داده‌های محلی موجود در نسخه مبنا حفظ شده‌اند.

## Build در GitHub
Workflow موجود در `.github/workflows/build.yml` از JDK 17، Android SDK 35 و Gradle 8.7 استفاده می‌کند و خروجی debug APK را به عنوان Artifact منتشر می‌کند.
