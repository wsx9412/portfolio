from django.urls import path, include

from . import views

app_name = 'classapp'
urlpatterns = [
  	path('', views.HomeView.as_view()),
    path('fix/', views.FixView.as_view(),name='fix'),  
    path('home/', views.HomeView.as_view(),name='home'),

    path('login/', views.LoginView.as_view(),name='login'),
    path('logout/', views.LogoutView.as_view(),name='logout'),

    path('assign/', views.AssignView.as_view(),name = 'assign'),
    path('myinfo/', views.MyInfoView.as_view(),name='myinfo'),

    path('register_select/',views.Registerview_select.as_view(),name = 'register_select'),
    path('register_p/',views.RegisterView_P.as_view(), name='register_p'),
    path('register_s/',views.RegisterView_S.as_view(), name='register_s'),
    
    path('board/list/',views.BoardListView.as_view(),name='board_list'),  # 게시판 목록
    path('board/<int:board_num>/',views.PostListView.as_view(),name='post_list'),  # 게시판 조회
    path('board/<int:board_num>/write/',views.PostWriteView.as_view(),name='post_write'),   # 게시글 작성
    path('board/<int:board_num>/<int:post_num>/',views.PostDetailView.as_view(),name='post_view'),  # 게시글 조회
    path('board/<int:board_num>/<int:post_num>/edit/',views.PostEditView.as_view(),name='post_edit'), # 게시글 수정
    path('board/<int:board_num>/<int:post_num>/delete/', views.PostDeleteView.as_view(), name = 'post_delete'), # 게시글 삭제
    path('board/<int:board_num>/<int:post_num>/<int:comment_num>/',views.CommentDeleteView.as_view(),name='comment_delete'), # 댓글 삭제

    path('board/edit/',views.BoardEditView.as_view(),name = 'board_edit'),
    path('board/edit/<str:board_name>/',views.BoardDeleteView.as_view(),name = 'board_delete'),
    path('board/create/',views.BoardCreateView.as_view(),name = 'board_create'),

    path('chat/list/',views.ChatroomListView.as_view(),name='chat_list'),  # 채팅방 목록 조회
    path('chat/create/',views.ChatcreateListView.as_view(),name = 'chat_create'),   # 채팅방 생성 [미완:세션구현]
    path('chat/<int:chat_num>/password',views.ChatPasswordView.as_view(),name='chat_pass'),  # 채팅방 비밀번호
    path('chat/<int:chat_num>/',views.ChatRoomView.as_view(),name='chat_room'),  # 채팅방

    path('group/create/', views.GroupCreateView.as_view(), name='group_create'), #그룹 생성 
    path('group/assign/', views.GroupAssignView.as_view(), name = 'group_assign'), #그룹 맴버 배정
    path('group/list', views.GroupListView.as_view(), name = 'group_list'),
]
