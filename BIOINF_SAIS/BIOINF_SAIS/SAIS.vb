Public Class SAIS

    Const MINBUCKETSIZE As Integer = 256
    Shared Sub Evaluate(S As Byte(), SA As Integer(), n As Integer)

        'check if input string length is 1, return SA
        If (n = 1) Then
            SA(0) = 0
            Return
        End If

        sais_main(New ByteArray(S, 0), SA, 0, n, MINBUCKETSIZE, False)

        Return


    End Sub

    Private Shared Sub sais_main(ByVal S As ByteArray, ByVal sa As Integer(), ByVal fs As Integer, ByVal n As Integer, ByVal k As Integer, ByVal isbwt As Boolean)

        Dim C, B, RA As IArray
        Dim flags As Integer

        If (k <= MINBUCKETSIZE) Then
            C = New IntArray(New Integer(k) {}, 0)
            If (k <= fs) Then
                B = New IntArray(sa, n + fs - k)
                flags = 1
            Else
                B = New IntArray(New Integer(k) {}, 0)
                flags = 3
            End If

        ElseIf (k <= fs) Then


        Else

        End If
    End Sub
End Class

Friend Interface IArray
    Default Property Item(i As Integer) As Integer

End Interface

Friend Class ByteArray
    Implements IArray
    Private m_array As Byte()
    Private m_pos As Integer
    Public Sub New(array As Byte(), pos As Integer)
        m_pos = pos
        m_array = array
    End Sub
    Protected Shadows Sub Finalize()
        Try
            m_array = Nothing
        Finally
            MyBase.Finalize()
        End Try
    End Sub
    Default Public Property Item(i As Integer) As Integer Implements IArray.Item
        Get
            Return CInt(m_array(i + m_pos))
        End Get
        Set(value As Integer)
            m_array(i + m_pos) = CByte(value)
        End Set
    End Property

End Class

Friend Class CharArray
    Implements IArray
    Private m_array As Char()
    Private m_pos As Integer
    Public Sub New(array As Char(), pos As Integer)
        m_pos = pos
        m_array = array
    End Sub
    Protected Shadows Sub Finalize()
        Try
            m_array = Nothing
        Finally
            MyBase.Finalize()
        End Try
    End Sub
    Default Public Property Item(i As Integer) As Integer Implements IArray.Item
        Get
            Return AscW(m_array(i + m_pos))
        End Get
        Set(value As Integer)
            m_array(i + m_pos) = ChrW(value)
        End Set
    End Property
End Class

Friend Class IntArray
    Implements IArray
    Private m_array As Integer()
    Private m_pos As Integer
    Public Sub New(array As Integer(), pos As Integer)
        m_pos = pos
        m_array = array
    End Sub
    Protected Overrides Sub Finalize()
        Try
            m_array = Nothing
        Finally
            MyBase.Finalize()
        End Try
    End Sub
    Default Public Property Item(i As Integer) As Integer Implements IArray.Item
        Get
            Return m_array(i + m_pos)
        End Get
        Set(value As Integer)
            m_array(i + m_pos) = value
        End Set
    End Property
End Class

Friend Class StringArray
    Implements IArray
    Private m_array As String
    Private m_pos As Integer
    Public Sub New(array As String, pos As Integer)
        m_pos = pos
        m_array = array
    End Sub
    Protected Overrides Sub Finalize()
        Try
            m_array = Nothing
        Finally
            MyBase.Finalize()
        End Try
    End Sub
    Default Public Property Item(i As Integer) As Integer Implements IArray.Item
        Get
            Return AscW(m_array(i + m_pos))
        End Get
        Set(value As Integer)
        End Set
    End Property
End Class





