B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=10.2
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.

	Private lblclass As Label
	Private lblid As Label
	Private lblclss As Label
	Private lblname As Label
	Private drawer As B4XDrawer
	Private lblme As Label
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	'Activity.LoadLayout("Layout1")
	drawer.Initialize(Me,"",Activity,60%x)
	drawer.CenterPanel.LoadLayout("profile")
	drawer.Leftpanel.Loadlayout("laydrawer")
	lblme.Text = Main.myfname&" "&Main.mylname
	
	lblname.Text = Main.myfname&" "&Main.mylname
	If Main.mylevel = 1 Then
		lblclss.Text = "Admin"
	else If Main.mylevel = 2 Then
		lblclss.Text = "Teacher"
	else If Main.mylevel = 3 Then
		lblclss.Text = "Student"
	Else
		lblclss.Text = "Your an alien"
	End If
	
	lblclass.Text = lblclss.Text & " ID:"
	lblid.Text = Main.myid
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub Label7_Click
	drawer.LeftOpen = True
End Sub


Sub lblme_Click
	StartActivity("profile")
End Sub

Sub lbllogout_Click
	Dim msg As Int
	msg = Msgbox2("Do You Want To Logout?","","Yes","No","",Null)
	If msg = DialogResponse.POSITIVE Then
		Activity.Finish
		StartActivity(Main)
	End If
End Sub

Sub lblhome_Click
	If Main.mylevel = 1 Then
		StartActivity("adminform")
		drawer.LeftOpen = False
	Else If Main.mylevel = 2 Then
		StartActivity("teacherform")
		drawer.LeftOpen = False
	Else
		StartActivity("studentform")
		drawer.LeftOpen = False
	End If
End Sub

Sub lblabout_Click
	StartActivity("about")
End Sub