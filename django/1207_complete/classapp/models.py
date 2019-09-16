from django.db import models
from datetime import datetime

class Admin(models.Model):
    email = models.CharField(max_length = 20,unique = True)
    password = models.CharField(max_length = 20)

class Student(models.Model):
    email = models.EmailField(unique = True)
    password = models.CharField(max_length = 20)
    number = models.IntegerField()

    def __str__(self):
        return self.email


class Professor(models.Model):
    email = models.EmailField(unique = True)
    password = models.CharField(max_length = 20)
    number = models.IntegerField()
    
    def __str__(self):
        return self.email


class Adviser(models.Model):
    professor = models.ForeignKey(Professor, on_delete = models.CASCADE)
    student = models.ForeignKey(Student, on_delete = models.CASCADE)

    def __str__(self):
        return "%s - %s" % (self.professor.email, self.student.email)
    
    class Meta:
        unique_together = (('professor', 'student'),)


class Group(models.Model):
    group_name = models.CharField(max_length=20,unique = True)

    def __str__(self):
        return self.group_name


class Groupmember(models.Model):
    group = models.ForeignKey(Group, on_delete = models.CASCADE)
    student = models.ForeignKey(Student, on_delete = models.CASCADE )

    def __str__(self):
        return "%s - %s" % (self.group.group_name, self.student.email)

    class Meta:
        unique_together = (('group', 'student'),)


class Board(models.Model):
    board_name = models.CharField(max_length = 20,unique = True)
    group_id = models.ForeignKey(Group, on_delete = models.CASCADE,null=True,blank=True)

    def __str__(self):
        return self.board_name


class BoardParticipant(models.Model):
    board = models.ForeignKey(Board, on_delete = models.CASCADE)
    student = models.ForeignKey(Student, on_delete = models.CASCADE,null=True,blank=True )
    professor = models.ForeignKey(Professor, on_delete = models.CASCADE,null=True,blank=True)

    def __str__(self):
        return "%s - %s" % (self.board.board_name, "test")


class Post(models.Model):
    board = models.ForeignKey(Board, on_delete = models.CASCADE)
    title = models.CharField(max_length = 20)
    contents = models.CharField(max_length = 500)
    writer_s = models.ForeignKey(Student, on_delete = models.CASCADE,null=True,blank=True)
    writer_p = models.ForeignKey(Professor, on_delete = models.CASCADE,null=True,blank=True)
    pub_date = models.DateTimeField(default = datetime.now)
    view_count = models.IntegerField(default=0)

    def __str__(self):
        return "%s - %s" % (self.board.board_name, self.title)

class Comment(models.Model):
    board = models.ForeignKey(Board, on_delete = models.CASCADE)
    post = models.ForeignKey(Post, on_delete = models.CASCADE)
    writer_s = models.ForeignKey(Student, on_delete = models.CASCADE,null=True,blank=True)
    writer_p = models.ForeignKey(Professor, on_delete = models.CASCADE,null=True,blank=True)
    comment = models.CharField(max_length = 200)
    pub_date = models.DateTimeField(default = datetime.now)

    def __str__(self):
        return "%s - %s - %s" % (self.board.board_name,self.post.title,self.comment)

    #class Meta:
    #    unique_together = (('board_num', 'post_num','id'),)    


class Chatroom(models.Model):
    name = models.CharField(max_length = 200)
    secret = models.BooleanField(default = False)
    password = models.CharField(max_length = 50,blank=True)
    manager_s = models.ForeignKey(Student, on_delete = models.CASCADE,null=True,blank=True)
    manager_p = models.ForeignKey(Professor, on_delete = models.CASCADE,null=True,blank=True)

    def __str__(self):
        return self.name


class Chatmember(models.Model):
    chatroom = models.ForeignKey(Chatroom, on_delete = models.CASCADE)
    member_s = models.ForeignKey(Student, on_delete = models.CASCADE,null=True,blank=True)
    member_p = models.ForeignKey(Professor, on_delete = models.CASCADE,null=True,blank=True)

    def __str__(self):
        retStr = "%s - " % (self.chatroom.name)

        if type(self.member_p) != type(None):
            retStr += self.member_p.email
        if type(self.member_s) != type(None):
            retStr += self.member_s.email

        return retStr

    #class Meta:
    #    unique_together = (('chatroom', 'member'),)

class Chatrecord(models.Model): 
    chatroom = models.ForeignKey(Chatroom, on_delete = models.CASCADE)
    member_s = models.ForeignKey(Student, on_delete = models.CASCADE,null=True,blank=True)
    member_p = models.ForeignKey(Professor, on_delete = models.CASCADE,null=True,blank=True)
    pub_date = models.DateTimeField(default = datetime.now)
    content = models.CharField(max_length = 1000)

    def __str__(self):
        retStr = "%s - " % self.chatroom.name
        
        if type(self.member_p) != type(None):
            retStr += self.member_p.email
        if type(self.member_s) != type(None):
            retStr += self.member_s.email
        
        retStr += ": %s" % self.content
        return retStr

    #class Meta:
    #    unique_together = (('chat_number', 'member_id','pub_date'),)