from django.contrib import admin
from my_app.models import User, Image
from django.contrib.auth.models import Group
from django.contrib.auth.admin import UserAdmin


class AccountAdmin(UserAdmin):
    list_display = ('email', 'date_joined', 'last_login', 'is_superuser')
    list_filter = ('is_superuser', 'is_staff', 'is_active')
    readonly_fields = ('date_joined', 'last_login')
    fieldsets = (
        (None, {'fields': ('email', 'password')}),
        ('Personal', {'fields': ('name', 'surname')}),
        ('Permissions', {'fields': ('is_superuser', 'is_staff', 'is_active',)}),
    )
    add_fieldsets = (
        (None, {
            'classes': ('wide',),
            'fields': ('email', 'password1', 'password2')}
         ),
    )
    search_fields = ('email',)
    ordering = ('email',)
    filter_horizontal = ()


admin.site.register(User, AccountAdmin)
admin.site.register(Image)
admin.site.unregister(Group)