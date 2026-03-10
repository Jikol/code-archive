from django.shortcuts import render, redirect, get_object_or_404
from django.contrib import messages
from django.contrib.auth import login, authenticate, logout
from django.contrib.auth.forms import PasswordChangeForm
from django.contrib.auth import update_session_auth_hash
from my_app.forms import (
    RegistrationForm,
    AccountAuthenticationForm,
    AccountUpdateForm,
    CreateImage
)
from my_app.models import User, Image


def login_view(request, context):
    user = request.user
    if user.is_authenticated:
        return redirect("home")

    if 'login' in request.POST:
        form_login = AccountAuthenticationForm(request.POST)
        if form_login.is_valid():
            email = request.POST['email']
            password = request.POST['password']
            user = authenticate(email=email, password=password)

            if user:
                login(request, user)
                return redirect("home")

    else:
        form_login = AccountAuthenticationForm()

    context['login_form'] = form_login
    return context


def home_view(request):
    context = {}
    images = Image.objects.filter(public=True)
    context['images'] = images
    login_view(request, context)
    context['title'] = 'Fotoalbum'
    return render(request, 'home.html', context)


def registration_view(request):
    context = {}
    if 'register' in request.POST:
        form_register = RegistrationForm(request.POST)
        if form_register.is_valid():
            logout(request)
            form_register.save()
            email = form_register.cleaned_data.get('email')
            raw_password = form_register.cleaned_data.get('password1')
            account = authenticate(email=email, password=raw_password)
            login(request, account)
            messages.success(request, f'Ucet s jmenem {email} vytvoren')
            return redirect('home')
        else:
            context['registration_form'] = form_register
    else:  # GET metoda
        form_register = RegistrationForm()
        context['registration_form'] = form_register

    login_view(request, context)
    context['title'] = 'Fotoalbum | registrace'
    return render(request, 'register.html', context)


def logout_view(request):
    logout(request)
    return redirect('home')


def account_view(request):
    context = {}
    context['user'] = User.objects.get(email=request.user)
    context['title'] = 'Fotoalbum | účet'
    return render(request, 'account.html', context)


def account_change_view(request):
    if not request.user.is_authenticated:
        return redirect('home')

    context = {}
    if 'account' in request.POST:
        account_form = AccountUpdateForm(request.POST, instance=request.user)
        user = User.objects.get(email=request.user)
        email = request.POST['email']
        name = request.POST['name']
        surname = request.POST['surname']
        if (not account_form.has_changed()) or (email == '' and name == '' and surname == ''):
            account_form = AccountUpdateForm(instance=request.user)
            messages.warning(request, f'Nic nebylo změněno')
        else:
            if account_form.is_valid():
                if email == '':
                    account_form.email = user.email
                if name == '':
                    account_form.name = user.name
                if surname == '':
                    account_form.surname = user.surname
                account_form.save()
                messages.success(request, f'Údaje pro {request.user} změněny')
            else:
                messages.error(request, f'Chyba validace')
    else:
        account_form = AccountUpdateForm(instance=request.user)

    context['account_form'] = account_form
    context['title'] = 'Fotoalbum | úprava údajů'
    return render(request, 'account_change.html', context)


def password_change_view(request):
    context = {}
    if 'password' in request.POST:
        password_form = PasswordChangeForm(data=request.POST, user=request.user)

        if password_form.is_valid():
            password_form.save()
            update_session_auth_hash(request, password_form.user)
            messages.success(request, f'Heslo pro {request.user} bylo změněno')
            return redirect('account')
        else:
            messages.warning(request, f'Hesla se neshodují / špatné původní heslo')

    else:
        password_form = PasswordChangeForm(user=request.user)

    context['password_form'] = password_form
    context['title'] = 'Fotoalbum | změna hesla'
    return render(request, 'password_change.html', context)


def upload_image_view(request):
    context = {}
    if not request.user.is_authenticated:
        messages.warning(request, f'Pro nahrání fotek musíš být přihlášen')
        return redirect('home')

    image_form = CreateImage(request.POST or None, request.FILES or None)
    if image_form.is_valid():
        obj = image_form.save(commit=False)
        user = User.objects.get(email=request.user)
        obj.user = user
        obj.save()
        image = Image.objects.filter(user=user).last()
        messages.success(request, f'Obrázek s název ' + image.title + ' nahrán')
        image_form = CreateImage()

    context['image_form'] = image_form
    context['title'] = 'Fotoalbum | nahrání'
    return render(request, "image_upload.html", context)


def account_images_view(request):
    context = {}
    if not request.user.is_authenticated:
        messages.warning(request, f'Pro zobrazení alba musíš být přihlášen')
        return redirect('home')

    images = Image.objects.filter(user=request.user)
    context['images'] = images
    context['title'] = 'Album | ' + str(request.user)
    return render(request, 'account_images.html', context)


def detail_image_view(request, slug):
    context = {}

    image = get_object_or_404(Image, slug=slug)
    context['image'] = image
    context['user'] = request.user
    context['title'] = 'Album | ' + str(request.user) + ' - ' + image.title
    return render(request, 'image_detail.html', context)