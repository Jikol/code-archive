from django.contrib import admin
from django.urls import path
from my_app import views
from django.conf import settings
from django.conf.urls.static import static
from django.urls import resolve

urlpatterns = [
    path('admin', admin.site.urls),
    path('', views.home_view, name='home'),
    path('register', views.registration_view, name='register'),
    path('logout', views.logout_view, name='logout'),
    path('account', views.account_view, name='account'),
    path('account/change', views.account_change_view, name='account_change'),
    path('password-change', views.password_change_view, name='password_change'),
    path('image/upload', views.upload_image_view, name='image_upload'),
    path('account/images', views.account_images_view, name='account_images'),
    path('account/images/<slug>', views.detail_image_view, name='detail_image'),
]

if settings.DEBUG:
    urlpatterns += static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
    urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)


