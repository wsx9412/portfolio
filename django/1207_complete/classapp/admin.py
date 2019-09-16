from django.contrib import admin

from classapp.models import *

admin.site.register(Admin)
admin.site.register(Student)
admin.site.register(Professor)
admin.site.register(Adviser)

admin.site.register(Group)
admin.site.register(Groupmember)

admin.site.register(Board)
admin.site.register(BoardParticipant)
admin.site.register(Post)
admin.site.register(Comment)

admin.site.register(Chatroom)
admin.site.register(Chatmember)
admin.site.register(Chatrecord)
