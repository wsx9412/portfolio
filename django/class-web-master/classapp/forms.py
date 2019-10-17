from django import forms
from . import models 
from django.db.models import Q

class LoginForm(forms.Form):
    user_id =  forms.CharField(max_length = 50)
    user_pw =  forms.CharField(max_length = 50,widget=forms.PasswordInput())

class RegisterForm_P(forms.Form):
    email_input = forms.EmailField(label = '이메일', max_length = 100)
    pass_input = forms.CharField(label = '비밀번호', max_length = 20)
    number_input = forms.IntegerField(label = '학번,직번', max_value = 999999999)

class RegisterForm_S(forms.Form):
    email_input = forms.EmailField(label = '이메일', max_length = 100)
    pass_input = forms.CharField(label = '비밀번호', max_length = 20)
    number_input = forms.IntegerField(label = '학번,직번', max_value = 999999999)

class CommentForm(forms.Form):
    comment_input = forms.CharField(max_length = 100)

class PostForm(forms.Form):
    title = forms.CharField(max_length = 20)
    content = forms.CharField(max_length = 500, widget=forms.Textarea)

class ChatForm(forms.Form):
    name = forms.CharField(label = '방이름',max_length=200)
    secret = forms.BooleanField(label ='비밀방여부',required=False)
    password = forms.CharField(label = '비밀번호',max_length = 50,required=False)


class ChatPasswordForm(forms.Form):
    password = forms.CharField(label="비밀번호",max_length=50)

class AssignForm(forms.Form):
    select_professor = forms.ModelChoiceField(models.Professor.objects.order_by('email'))
    select_student = forms.ModelChoiceField(models.Student.objects.order_by('email'))

CHOICES = [("Professor","Professor"),("Student","Student")]

class Register_choice(forms.Form):
    choice = forms.ChoiceField(label = '',choices=CHOICES,widget = forms.RadioSelect)

class MyinfoForm(forms.Form):
    pass_fix = forms.CharField(label = '비밀번호', max_length=20)
    number_fix = forms.IntegerField(label = '학번, 직번', max_value= 99999999)

class GroupCreateForm(forms.Form):
    name_input = forms.CharField(max_length = 200)

class GroupAssignForm(forms.Form):
    select_group = forms.ModelChoiceField(models.Group.objects.order_by('group_name'))
    select_member = forms.ModelChoiceField(models.Student.objects.order_by('email'))

class Board(forms.Form):
    board_name = forms.CharField(label = '게시판이름',max_length=20)
    group_id = forms.ModelChoiceField(models.Group.objects.order_by('group_name'))