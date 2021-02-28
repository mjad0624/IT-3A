B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=10.2
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: True
	#IncludeTitle: False
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim sql As SQL
	

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.


	Private etlname As EditText
	Private etfname As EditText
	Private etpwd As EditText
	Private etuname As EditText
	Private etid As EditText
	Private etlevel As EditText
End Sub

Sub Activity_Create(FirstTime As Boolean)
	Activity.LoadLayout("adduser")
	If FirstTime Then
		copydbifneeded("database.db")
		sql.Initialize(File.DirInternal,"database.db",False)
	End If


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


Sub btncancel_Click
	Activity.Finish
End Sub

Sub btnadd_Click
	Dim qry As String
	Dim c1 As Cursor
	If etuname.text = "" Or etpwd.text = "" Or etfname.text = "" Or etlname.text = "" Or etlevel.text = "" Then
		Msgbox("Please Fill all required data","Error")
	Else 
		If etlevel.Text > 4 Or etlevel.Text <= 0 Then
				Msgbox("Invalid Level","Error")
			
		Else
			qry = "Select * From users where id = ?"
			c1 = sql.ExecQuery2(qry,Array As String(etuname.Text))
			If c1.RowCount>0 Then
				ToastMessageShow("Username Already Exist",False)
			Else
				qry = "Insert into users(uname, password,fname,lname,level) values(?,?,?,?,?)"
				sql.ExecNonQuery2(qry, Array As String(etuname.Text,etpwd.Text,etfname.Text,etlname.Text,etlevel.Text))
				Msgbox("User Successfully Added","")
				Activity.Finish
			End If
		End If
	End If
End Sub
