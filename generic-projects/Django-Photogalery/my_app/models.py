from django.db import models
from django.contrib.auth.models import AbstractBaseUser, BaseUserManager
from django.utils.text import slugify
from django.conf import settings
from django.db.models.signals import post_delete, pre_save
from django.dispatch import receiver
from PIL import Image as IMAGE
from io import BytesIO
from django.core.files.uploadedfile import InMemoryUploadedFile
import sys, glob, os


def upload_location(instance, filename, **kwargs):
    file_path = 'photos/{user_id}/{title}-{filename}'.format(
        user_id=str(instance.user.id), title=str(instance.title), filename=filename
    )
    return file_path


def thumbnail_location(instance, filename, **kwargs):
    file_path = 'photos/{user_id}/thumbnail/{title}-thumbnail-{filename}'.format(
        user_id=str(instance.user.id), title=str(instance.title), filename=filename
    )
    return file_path


class UserManager(BaseUserManager):
    def create_user(self, email, password=None):
        if not email:
            raise ValueError("Email je povinný")
        if not password:
            raise ValueError("Heslo je povinné")

        user = self.model(
            email=self.normalize_email(email)
        )
        user.set_password(password)
        user.save(using=self._db)
        return user

    def create_superuser(self, email, password):
        user = self.create_user(
            email=self.normalize_email(email),
            password=password
        )
        user.is_superuser = True
        user.is_staff = True
        user.save(using=self._db)
        return user


class User(AbstractBaseUser):
    email = models.EmailField('Email', blank=False, max_length=60, unique=True)
    name = models.CharField('Name', blank=True, max_length=20)
    surname = models.CharField('Surname', blank=True, max_length=30)
    date_joined = models.DateTimeField('Date joined', blank=False, auto_now_add=True)
    last_login = models.DateTimeField('Last login', blank=False, auto_now=True)
    is_superuser = models.BooleanField('Admin', default=False)
    is_active = models.BooleanField('Active', blank=False, default=True)
    is_staff = models.BooleanField('Staff', blank=False, default=False)

    USERNAME_FIELD = 'email'  # username
    REQUIRED_FIELDS = []

    objects = UserManager()

    def __str__(self):
        return self.email

    def has_perm(self, perm, obj=None):
        return self.is_superuser

    def has_module_perms(self, app_label):
        return True

    class Meta:
        verbose_name = 'user'
        verbose_name_plural = 'users'


class Image(models.Model):
    title = models.CharField('Title', blank=False, max_length=30, unique=True)
    image = models.ImageField('Image', upload_to=upload_location, blank=False)
    desc = models.TextField('Description', blank=True, max_length=150)
    thumbnail = models.ImageField('Thumbnail', upload_to=thumbnail_location, blank=False)
    date_uploaded = models.DateTimeField('Date of image uploaded', blank=False, auto_now_add=True)
    date_updated = models.DateTimeField('Date of image updated', blank=False, auto_now=True)
    public = models.BooleanField('Publicity of image', blank=False)
    slug = models.SlugField(blank=True, unique=True)
    user = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE)

    def __str__(self):
        return self.title

    class Meta:
        verbose_name = 'image'
        verbose_name_plural = 'images'

    def save(self):
        image = IMAGE.open(self.image)
        output = BytesIO()
        image.save(output, format='PNG', quality=100)
        output.seek(0)
        self.image = InMemoryUploadedFile(output, 'ImageField', "%s.png" % self.image.name.split('.')[0], 'image/png', sys.getsizeof(output), None)

        size = 500, 500
        image = IMAGE.open(self.image)
        output = BytesIO()
        image.thumbnail(size)
        image.save(output, format='PNG', quality=100)
        output.seek(0)
        self.thumbnail = InMemoryUploadedFile(output, 'ImageField', "%s.png" % self.thumbnail.name.split('.')[0], 'image/png', sys.getsizeof(output), None)

        super(Image, self).save()


@receiver(post_delete, sender=Image)
def cascade_delete(sender, instance, **kwargs):
    instance.image.delete(False)
    instance.thumbnail.delete(False)


def pre_save_image_recaiver(sender, instance, *args, **kwargs):
    if not instance.slug:
        instance.slug = slugify(str(instance.user.id) + "-" + instance.title)


pre_save.connect(pre_save_image_recaiver, sender=Image)


"""
class Post(models.Model):
    content = models.TextField('Content', blank=False, max_length=50)
    date_created = models.DateTimeField('Date_of_comment_create', default=timezone.now)
    likes = models.DecimalField('Likes', max_digits=4, decimal_places=0, blank=True)
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    image = models.ForeignKey(Image, on_delete=models.CASCADE)

    def get_likes(self):
        return '%s' % self.likes

    def __str__(self):
        return self.content

    class Meta:
        verbose_name = 'post'
        verbose_name_plural = 'posts'
"""