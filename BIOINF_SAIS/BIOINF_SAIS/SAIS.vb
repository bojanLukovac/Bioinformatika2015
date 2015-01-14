
Imports System.Diagnostics.Eventing.Reader
Imports Microsoft.VisualBasic.CompilerServices

Class SAIS

    Shared Sub Evaluate(S As Byte(), SA As Integer(), n As Integer)

        'initialize t
        Dim t(n - 1) As Boolean
        t(n - 1) = True
        For i As Integer = n - 2 To 0 Step -1
            If (S(i) < S(i + 1) Or S(i).ToString() = "$") Then
                t(i) = True
            Else
                t(i) = False
            End If
        Next

        'set P 
        'k-1 is the pointer to the last 
        Dim P_tmp(n - 1) As Integer

        Dim k As Integer = 0
        For j As Integer = n - 1 To 1 Step -1
            If ((t(j) = True And t(j - 1) = False) Or S(j).ToString() = "$") Then
                P_tmp(k) = j
                k += 1
            End If
        Next
        'reverse P and discard 0
        Dim P1(k - 1) As Integer
        For i As Integer = 0 To k - 1
            P1(i) = P_tmp(k - 1)
            k -= 1
        Next

        'count buckets 
        Dim Counts As New SortedDictionary(Of Object, Integer)
        For Each b As Byte In S
            If (Counts.ContainsKey(b)) Then
                Counts.Item(b) += 1
            Else
                Counts.Add(b, 1)
            End If
        Next

        'initialize B and SA
        Dim BucketPointers As New SortedDictionary(Of Byte, Integer)
        'Dim SA_tmp As New Dictionary(Of Byte, Array)
        For i As Integer = 0 To SA.Length - 1
            SA(i) = -1
        Next
        'set bucket pointers to ends
        Dim previous As Integer = 0
        For Each pair As KeyValuePair(Of Object, Integer) In Counts
            BucketPointers(pair.Key) = previous + pair.Value - 1
            previous += pair.Value
        Next

        'STEP1
        'fill SA with indexes of LMS suffixes from beginning 
        'pocetak podpolja BucketPointers(b) - Counts(b)
        Dim index As Integer = 0
        For Each b As Byte In S
            If (P1.Contains(index)) Then
                SA(BucketPointers(b)) = index
                BucketPointers(b) -= 1
            End If
            index += 1
        Next

        'STEP2
        'set bucket pointers to beginnings
        previous = 0
        For Each pair As KeyValuePair(Of Object, Integer) In Counts
            BucketPointers(pair.Key) = previous
            previous += pair.Value
        Next

        'iduce SAI
        For i As Integer = 0 To SA.Length - 1
            If (SA(i) > 0) Then
                If (t(SA(i) - 1) = False) Then
                    SA(BucketPointers(S(SA(i) - 1))) = SA(i) - 1
                    BucketPointers(S(SA(i) - 1)) += 1
                End If
            End If
        Next

        'STEP 3
        'set bucket pointers to ends
        previous = 0
        For Each pair As KeyValuePair(Of Object, Integer) In Counts
            BucketPointers(pair.Key) = previous + pair.Value - 1
            previous += pair.Value
        Next

        'induce SAs
        For i As Integer = SA.Length - 1 To 0 Step -1
            If (SA(i) > 0) Then
                If (t(SA(i) - 1) = True) Then
                    SA(BucketPointers(S(SA(i) - 1))) = SA(i) - 1
                    BucketPointers(S(SA(i) - 1)) -= 1
                End If
            End If
        Next

        'name LMS substrings by it's buckets
        Dim lmsName As Integer = 0
        Dim lmsPrev As Integer = SA(0)
        Dim S1(P1.Length - 1) As Byte
        Dim lmsCurr As Integer
        For i As Integer = 0 To n - 1
            If (P1.Contains(SA(i))) Then
                lmsCurr = SA(i)
                Do
                    If (lmsCurr = n - 1) Then
                        Exit Do
                    End If

                    If (S(lmsCurr) <> S(lmsPrev)) Then
                        lmsName += 1
                        Exit Do
                    ElseIf (t(lmsCurr) = t(lmsPrev)) Then
                        lmsCurr += 1
                        lmsPrev += 1
                    End If

                Loop While (t(SA(lmsCurr)) = False)

                S1(Array.IndexOf(P1, SA(i))) = lmsName
                lmsPrev = lmsCurr
            End If
        Next

        'check if elements in S1 are unique
        Dim unique As Boolean = True
        For Each element As Integer In S1
            For i As Integer = 0 To S1.Length - 1
                If (element = S1(i)) Then
                    unique = False
                    Exit For
                End If
                If (Not (unique)) Then
                    Exit For
                End If
            Next
        Next

        'check if recursion is needed
        Dim SA1(S1.Length - 1) As Integer
        If (unique) Then
            For i As Integer = 0 To S1.Length-1
                SA1(S(i)) = i
            Next
        Else
            SAIS.Evaluate(S1, SA1, S1.Length)
        End If

        ''induce SA from SA1
        'For i As Integer = 0 To SA.Length-1
        '    SA(i) = -1
        'Next
        ''set bucket pointers to ends
        'previous = 0
        'For Each pair As KeyValuePair(Of Object, Integer) In Counts
        '    BucketPointers(pair.Key) = previous + pair.Value - 1
        '    previous += pair.Value
        'Next
        'For i As Integer = SA1.Length - 1 To 0
        '    P1(SA1(i))
        'Next



    End Sub
End Class
