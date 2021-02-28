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
	Private drawer As B4XDrawer
	Private lblme As Label
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	'Activity.LoadLayout("Layout1")
	
	drawer.Initialize(Me,"",Activity,60%x)
	drawer.CenterPanel.LoadLayout("about")
	drawer.Leftpanel.Loadlayout("laydrawer")
	lblme.Text = Main.myfname&" "&Main.mylname
    

End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub Label2_Click
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
		drawer.LeftOpen = False
		StartActivity("adminform")
		
	Else If Main.mylevel = 2 Then
		drawer.LeftOpen = False
		StartActivity("teacherform")
		
	Else
		drawer.LeftOpen = False
		StartActivity("studentform")
		
	End If
End Sub

Sub lblabout_Click
	drawer.LeftOpen = False
End Sub