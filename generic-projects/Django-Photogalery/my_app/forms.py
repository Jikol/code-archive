from django import forms
from django.contrib.auth.forms import UserCreationForm
from django.contrib.auth import authenticate
from my_app.models import User, Image


class RegistrationForm(UserCreationForm):
    email = forms.EmailField(label='Email', max_length=64, min_length=4, required=True, widget=forms.EmailInput(attrs={'class': 'form-control register-email', 'placeholder': 'Zadej email...'}))
    password1 = forms.CharField(label='Heslo', widget=forms.PasswordInput(attrs={'class': 'form-control register-password1', 'placeholder': 'Zadej heslo...'}))
    password2 = forms.CharField(label='Heslo znova', widget=forms.PasswordInput(attrs={'class': 'form-control register-password2', 'placeholder': 'Heslo znova pro kontrolu...'}))
    name = forms.CharField(label='Jméno (nepovinné)', max_length=32, min_length=3, required=False, widget=forms.TextInput(attrs={'class': 'form-control register-name', 'placeholder': 'Zadej jméno...'}))
    surname = forms.CharField(label='Příjmení (nepovinné)', max_length=64, min_length=3, required=False, widget=forms.TextInput(attrs={'class': 'form-control register-surname', 'placeholder': 'Zadej příjemní...'}))

    class Meta:
        model = User
        fields = ("email", "password1", "password2", "name", "surname")


class AccountAuthenticationForm(forms.ModelForm):
    email = forms.EmailField(label='Email', required=True, widget=forms.EmailInput(attrs={'class': 'form-control', 'placeholder': 'Email'}))
    password = forms.CharField(label='Heslo', required=True, widget=forms.PasswordInput(attrs={'class': 'form-control', 'placeholder': 'Heslo'}))

    class Meta:
        model = User
        fields = ('email', 'password')

    def clean(self):
        email = self.cleaned_data['email']
        password = self.cleaned_data['password']
        if not authenticate(email=email, password=password):
            raise forms.ValidationError('Špatné jméno nebo heslo')


class AccountUpdateForm(forms.ModelForm):
    email = forms.EmailField(label='Email', max_length=64, min_length=4, required=False, help_text="email")
    name = forms.CharField(label='Jméno', max_length=32, min_length=3, required=False, help_text="text")
    surname = forms.CharField(label='Příjmení', max_length=64, min_length=3, required=False, help_text="text")

    class Meta:
        model = User
        fields = ('email', 'name', 'surname')

    def clean_email(self):
        if self.is_valid():
            email = self.cleaned_data['email']
            try:
                user = User.objects.exclude(pk=self.instance.pk).get(email=email)
            except User.DoesNotExist:
                return email
            raise forms.ValidationError('Email %s již existuje' % user)

    def clean_name(self):
        if self.is_valid():
            name = self.cleaned_data['name']
            return name

    def clean_surname(self):
        if self.is_valid():
            surname = self.cleaned_data['surname']
            return surname

    def change_email(self, email):
        self.email = email


class CreateImage(forms.ModelForm):
    title = forms.CharField(label='Titulek', required=True, max_length=30, min_length=3, widget=forms.TextInput(attrs={'class': 'form-control image-upload-control'}), help_text="text")
    desc = forms.CharField(label_suffix='Popis', required=False, max_length=150, min_length=15, widget=forms.Textarea(attrs={'class': 'form-control image-upload-control'}), help_text="text")
    image = forms.ImageField(label='Soubor obrázku', required=True, widget=forms.FileInput(attrs={'class': 'form-control image-upload-control'}), help_text="image")
    public = forms.BooleanField(label='Veřejný obrázek', required=False, widget=forms.CheckboxInput(attrs={'class': 'form-control image-upload-control'}), help_text="checkbox")

    class Meta:
        model = Image
        fields = ['title', 'desc', 'image', 'public']

