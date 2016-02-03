Attribute VB_Name = "MlvvMarkjeetaajs"
Sub Markjeetaajs()
'
' Markjeetaajs Macro
'
'
    Application.ScreenUpdating = False
    
    Priekshdarbi
    DubultoTaguIeliceejs
    VienkaarshoTaguIeliceejs
    MsgBox "Viss kârtîbâ!", 0, "Maríçtâjs"
    Application.ScreenUpdating = True
End Sub

Sub VienkaarshoTaguIeliceejs()
    Dim oRange As Range
    Set oRange = ActiveDocument.Range
    
    With oRange.Find
        .text = ChrW(61614)
        .Replacement.text = "<arrow/>"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .MatchWholeWord = True
        .Execute Replace:=wdReplaceAll
    End With
    With oRange.Find
        .text = ChrW(61551)
        .Replacement.text = "<square/>"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .MatchWholeWord = True
        .Execute Replace:=wdReplaceAll
    End With
    With oRange.Find
        .text = ChrW(9675)
        .Replacement.text = "<circle/>"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .MatchWholeWord = True
        .Execute Replace:=wdReplaceAll
    End With
    With oRange.Find
        .text = ChrW(8710)
        .Replacement.text = "<triangle/>"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .MatchWholeWord = True
        .Execute Replace:=wdReplaceAll
    End With
    With oRange.Find
        .text = ChrW(61618)
        .Replacement.text = "<diamond/>"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .MatchWholeWord = True
        .Execute Replace:=wdReplaceAll
    End With
    With oRange.Find
        .text = ChrW(8226)
        .Replacement.text = "<bullet/>"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .MatchWholeWord = True
        .Execute Replace:=wdReplaceAll
    End With
    With oRange.Find
        .text = ChrW(-3913)
        .Replacement.text = "<bullet/>"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .MatchWholeWord = True
        .Execute Replace:=wdReplaceAll
    End With
    With oRange.Find
        .text = "•"
        .Replacement.text = "<bullet/>"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .MatchWholeWord = True
        .Execute Replace:=wdReplaceAll
    End With
    With oRange.Find
        .text = "//"
        .Replacement.text = "<lines/>"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .MatchWholeWord = True
        .Execute Replace:=wdReplaceAll
    End With

End Sub

Sub DubultoTaguIeliceejs()
    Dim oRange As Range
    
    Set oRange = ActiveDocument.Range
    With oRange.Find
        .text = ""
        .Format = True
        .Highlight = True
        Flag = .Execute
        While Flag = True
            .Parent.InsertBefore "<high>"
            .Parent.InsertAfter "</high>"
            .Font.Reset
            oRange.SetRange Start:=.Parent.End, End:=ActiveDocument.Range.End
            With oRange.Find
                .text = ""
                .Format = True
                .Highlight = True
                Flag = .Execute
            End With
        Wend
    End With
    NovaaktFormateejumu ("<high>")
    NovaaktFormateejumu ("</high>")
    
    Set oRange = ActiveDocument.Range
    oRange.Find.Highlight = True
    oRange.Find.Forward = True
    Do While oRange.Find.Execute
        If oRange.HighlightColorIndex = WdColorIndex.wdGray25 Then
            oRange.Find.Parent.InsertBefore "<gray>"
            oRange.Find.Parent.InsertAfter "</gray>"
        End If
        intPosition = oRange.End
        oRange.Start = intPosition
    Loop
    
    Set oRange = ActiveDocument.Range
    oRange.Find.Highlight = True
    oRange.Find.Forward = True
    Do While oRange.Find.Execute
        If oRange.HighlightColorIndex = WdColorIndex.wdGray50 Then
            oRange.Find.Parent.InsertBefore "<gray>"
            oRange.Find.Parent.InsertAfter "</gray>"
        End If
        intPosition = oRange.End
        oRange.Start = intPosition
    Loop
    
    Set oRange = ActiveDocument.Range
    With oRange.Find
        .text = ""
        .Format = True
        .Font.Superscript = True
        Flag = .Execute
        While Flag = True
            .Parent.InsertBefore "<sup>"
            .Parent.InsertAfter "</sup>"
            .Font.Reset
            oRange.SetRange Start:=.Parent.End, End:=ActiveDocument.Range.End
            With oRange.Find
                .text = ""
                .Format = True
                .Font.Superscript = True
                Flag = .Execute
            End With
        Wend
    End With
    NovaaktFormateejumu ("<sup>")
    NovaaktFormateejumu ("</sup>")
    
    Set oRange = ActiveDocument.Range
    With oRange.Find
        .text = ""
        .Format = True
        .Font.Subscript = True
        Flag = .Execute
        While Flag = True
            .Parent.InsertBefore "<sub>"
            .Parent.InsertAfter "</sub>"
            .Font.Reset
            oRange.SetRange Start:=.Parent.End, End:=ActiveDocument.Range.End
            With oRange.Find
                .text = ""
                .Format = True
                .Font.Subscript = True
                Flag = .Execute
            End With
        Wend
    End With
    NovaaktFormateejumu ("<sub>")
    NovaaktFormateejumu ("</sub>")
    
    Set oRange = ActiveDocument.Range
    With oRange.Find
        .text = ""
        .Format = True
        .Font.Underline = True
        Flag = .Execute
        While Flag = True
            .Parent.InsertBefore "<u>"
            .Parent.InsertAfter "</u>"
            .Font.Reset
            oRange.SetRange Start:=.Parent.End, End:=ActiveDocument.Range.End
            With oRange.Find
                .text = ""
                .Format = True
                .Font.Underline = True
                Flag = .Execute
            End With
        Wend
    End With
    NovaaktFormateejumu ("<u>")
    NovaaktFormateejumu ("</u>")
    
    Set oRange = ActiveDocument.Range
    With oRange.Find
        .text = ""
        .Format = True
        .Font.Bold = True
        Flag = .Execute
        While Flag = True
            .Parent.InsertBefore "<b>"
            .Parent.InsertAfter "</b>"
            .Font.Reset
            oRange.SetRange Start:=.Parent.End, End:=ActiveDocument.Range.End
            With oRange.Find
                .text = ""
                .Format = True
                .Font.Bold = True
                Flag = .Execute
            End With
        Wend
    End With
    NovaaktFormateejumu ("<b>")
    NovaaktFormateejumu ("</b>")
    
    Set oRange = ActiveDocument.Range
    With oRange.Find
        .text = ""
        .Format = True
        .Font.Italic = True
        Flag = .Execute
        While Flag = True
            .Parent.InsertBefore "<i>"
            .Parent.InsertAfter "</i>"
            .Font.Reset
            oRange.SetRange Start:=.Parent.End, End:=ActiveDocument.Range.End
            With oRange.Find
                .text = ""
                .Format = True
                .Font.Italic = True
                Flag = .Execute
            End With
        Wend
    End With
    NovaaktFormateejumu ("<i>")
    NovaaktFormateejumu ("</i>")
    
    Set oRange = ActiveDocument.Range
    With oRange.Find
        .text = ""
        .Format = True
        .Font.Spacing = 3
        Flag = .Execute
        While Flag = True
            .Parent.InsertBefore "<extended>"
            .Parent.InsertAfter "</extended>"
            .Font.Reset
            oRange.SetRange Start:=.Parent.End, End:=ActiveDocument.Range.End
            With oRange.Find
                .text = ""
                .Format = True
                .Font.Spacing = 3
                Flag = .Execute
            End With
        Wend
    End With
    NovaaktFormateejumu ("<extended>")
    NovaaktFormateejumu ("</extended>")
    

End Sub

Sub Priekshdarbi()

    Dim oRange As Range
    Set oRange = ActiveDocument.Range
    With oRange.Find
        .text = " ^p"
        .Replacement.text = "^p"
        .Forward = True
        .Wrap = wdFindContinue
        .Execute Replace:=wdReplaceAll
    End With

    NovaaktFormateejumu ("^p")
End Sub

Sub NovaaktFormateejumu(text)
    Dim oRange As Range
    Set oRange = ActiveDocument.Range
    With oRange.Find
        .text = text
        .Replacement.text = text
        .Replacement.Font.Underline = False
        .Replacement.Font.Bold = False
        .Replacement.Font.Italic = False
        .Replacement.Font.Subscript = False
        .Replacement.Font.Superscript = False
        .Replacement.Font.Spacing = 0
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .Execute Replace:=wdReplaceAll
    End With
End Sub
