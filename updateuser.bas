B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=10.2
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: false
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim	uid As String	
	Dim uname As String
	Dim upass As String
	Dim ufname As String
	Dim ulname As String
	Dim ulevel As String
	Dim sql As SQL
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.

	Private etid As EditText
	Private etuname As EditText
	Private etpass As EditText
	Private etfname As EditText
	Private etlname As EditText
	Private etlevel As EditText
	Private drawer as B4XDrawer

End Sub

Sub Activity_Create(FirstTime As Boolean)
	Activity.LoadLayout("updateuser")
	If FirstTime Then
		copydbifneeded("database.db")
		sql.Initialize(File.DirInternal,"database.db",False)
	End If

	
	etid.Text = uid
	etuname.Text = uname
	etpass.Text = upass
	etfname.Text = ufname
	etlname.Text = ulname
	etlevel.Text = ulevel

End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub copydbifneeded(Filename As String)
	If File.Exists(File.DirInternal,Filename) = False Then
		File.Copy(File.DirAssets,Filename,File.DirInternal,Filename)
	End If
End Sub

Sub btnupdate_Click
	Dim qry As String
	Dim ans As String
	
	ans = Msgbox2("Save Changes?","Update Records","Ok","Cancel","",Null)
	If ans = DialogResponse.POSITIVE Then
		If etuname.Text = "" Or etpass.Text = ""  Or etfname.Text = "" Or etlname.Text="" Or etlevel.Text = "" Then
			Msgbox("Please Fill all required data","Error")
		Else
			If etlevel.Text > 4 Or etlevel.Text <= 0 Then
				Msgbox("Invalid Level","Error")
			Else
				qry = "Update users set uname = ?, password = ?, fname = ?, lname = ?, level = ? where id = "& etid.Text
				sql.ExecNonQuery2(qry, Array As String (etuname.Text,etpass.Text,etfname.Text,etlname.Text,etlevel.Text))
				Msgbox("User Successfully Updated","")
				Activity.Finish
			End If
		End If
	Else
		Msgbox("Update cancelled)","")
	End If
End Sub

Sub btncancel_Click
	Activity.Finish
End Sub

Sub Label8_Click
	drawer.LeftOpen = True
End Sub


Sub lblme_Click
	drawer.LeftOpen = False
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
	StartActivity("about")
End Sub