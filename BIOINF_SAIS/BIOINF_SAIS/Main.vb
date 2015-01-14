Module Main

    Sub Main(args As String())

        'cmd usage: main.exe filename.txt
        'Main calls the function SAIS(S,SA,n) where S is an input string, SA is an output array and n is the length of the input string
        If (args.Count <> 1) Then
            Console.WriteLine("You must specify one input file.")
            Console.WriteLine("Press enter to terminate..")
            Console.Read()
            Return
        End If

        Dim filename As String = "testovi/test5.txt"

        Dim fs As System.IO.FileStream = New IO.FileStream(filename, IO.FileMode.Open, IO.FileAccess.Read)

        Console.WriteLine(filename + ": {0} bytes", fs.Length)

        'declare S as input string
        Dim Bytes(fs.Length - 1) As Byte

        'declare SA array as output suffix array of S
        Dim SA(fs.Length - 1) As Integer

        fs.Read(Bytes, 0, Bytes.Length)

        'check if input string exists
        If (IsNothing(Bytes) Or IsNothing(SA) Or (Bytes.Length = 0)) Then
            Console.WriteLine("An error occured. Check your input string.")
            Console.WriteLine("Press enter to terminate...")
            Console.Read()
            Return
        End If

        Dim S(Bytes.Length - 1) As Integer
        Dim index As Integer = 0
        For Each b As Byte In Bytes
            S(index) = b
            index += 1
        Next

        Dim sw As System.Diagnostics.Stopwatch = New System.Diagnostics.Stopwatch()

        sw.Start()
        'starts the SAIS algorithm
        SAIS.Evaluate(S, SA, S.Length)
        sw.Stop()

        'write on output the final result - SA :
        Console.Write("SA=")
        For Each i As Integer In SA
            Console.Write("{0} ", i)
        Next
        Console.WriteLine()

        Dim elapsedTime As Double = sw.ElapsedTicks / System.Diagnostics.Stopwatch.Frequency
        Console.WriteLine("Elapsed time: {0:f8}s", elapsedTime)

        S = Nothing
        SA = Nothing

        Console.Read()
    End Sub

End Module
