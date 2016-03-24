Sub Markjeetaajs()
'
' Markjeetaajs Macro
'

    Application.ScreenUpdating = False
    
    Priekshdarbi
    VienkaarshoTaguIeliceejs
    DubultoTaguIeliceejs
    MsgBox "Viss gatavs!", 0, "MLVV"
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
    ' Sasodîts aprisinâjums tam, ka Word reizçm nepareizi eksportç []
    With oRange.Find
        .text = "["
        .Replacement.text = "<openSquareBrack/>"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .MatchWholeWord = True
        .Execute Replace:=wdReplaceAll
    End With
    ' Sasodîts aprisinâjums tam, ka Word reizçm nepareizi eksportç []
    With oRange.Find
        .text = "]"
        .Replacement.text = "<closeSquareBrack/>"
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
        .text = ChrW(-3928)
        .Replacement.text = "<square/>"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .MatchWholeWord = True
        .Execute Replace:=wdReplaceAll
    End With
    NovaaktFormateejumu ("<square/>")
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
    NovaaktFormateejumu ("<circle/>")
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
        .text = ChrW(916)
        .Replacement.text = "<triangle/>"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .MatchWholeWord = True
        .Execute Replace:=wdReplaceAll
    End With
    NovaaktFormateejumu ("<triangle/>")
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
    NovaaktFormateejumu ("<diamond/>")
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
        .text = ChrW(9679)
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
    NovaaktFormateejumu ("<bullet/>")
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
    NovaaktFormateejumu ("<lines/>")
End Sub

Sub DubultoTaguIeliceejs()
    Dim oRange As Range
    
	'Set oldRange = ActiveDocument.Content
    'With oldRange.Find
    '    .text = ""
    '    .Format = True
    '    .Highlight = True
    '    Flag = .Execute
    '    With .Replacement
    '        .ClearFormatting
    '        .text = "<high>^&</high>"
    '    End With
    '    .Forward = True
    '    .Wrap = wdFindContinue
    '    '.MatchWildcards = True
    '    .Execute Replace:=wdReplaceAll
    'End With
    'NovaaktFormateejumu ("<high>")
    'NovaaktFormateejumu ("</high>")
    
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
    
	Set oldRange = ActiveDocument.Content
    With oldRange.Find
        .text = ""
        .Format = True
        .Font.Superscript = True
        Flag = .Execute
        With .Replacement
            .ClearFormatting
            .text = "<sup>^&</sup>"
        End With
        .Forward = True
        .Wrap = wdFindContinue
        '.MatchWildcards = True
        .Execute Replace:=wdReplaceAll
    End With
    NovaaktFormateejumu ("<sup>")
    NovaaktFormateejumu ("</sup>")
    
	Set oldRange = ActiveDocument.Content
    With oldRange.Find
        .text = ""
        .Format = True
        .Font.Subscript = True
        Flag = .Execute
        With .Replacement
            .ClearFormatting
            .text = "<sub>^&</sub>"
        End With
        .Forward = True
        .Wrap = wdFindContinue
        '.MatchWildcards = True
        .Execute Replace:=wdReplaceAll
    End With
    NovaaktFormateejumu ("<sub>")
    NovaaktFormateejumu ("</sub>")
    
    Set oldRange = ActiveDocument.Content
    With oldRange.Find
        .text = ""
        .Format = True
        .Font.Underline = True
        Flag = .Execute
        With .Replacement
            .ClearFormatting
            .text = "<u>^&</u>"
        End With
        .Forward = True
        .Wrap = wdFindContinue
        '.MatchWildcards = True
        .Execute Replace:=wdReplaceAll
    End With
    NovaaktFormateejumu ("<u>")
    NovaaktFormateejumu ("</u>")
    
    Set oldRange = ActiveDocument.Content
    With oldRange.Find
        .text = ""
        .Format = True
        .Font.Bold = True
        Flag = .Execute
        With .Replacement
            .ClearFormatting
            .text = "<b>^&</b>"
        End With
        .Forward = True
        .Wrap = wdFindContinue
        '.MatchWildcards = True
        .Execute Replace:=wdReplaceAll
    End With
    NovaaktFormateejumu ("<b>")
    NovaaktFormateejumu ("</b>")
    
    Set oldRange = ActiveDocument.Content
    With oldRange.Find
        .text = ""
        .Format = True
        .Font.Italic = True
        Flag = .Execute
        With .Replacement
            .ClearFormatting
            .text = "<i>^&</i>"
        End With
        .Forward = True
        .Wrap = wdFindContinue
        '.MatchWildcards = True
        .Execute Replace:=wdReplaceAll
    End With
    NovaaktFormateejumu ("<i>")
    NovaaktFormateejumu ("</i>")
    
    Set oldRange = ActiveDocument.Content
    With oldRange.Find
        .text = ""
        .Format = True
        .Font.Spacing = 3
        Flag = .Execute
        With .Replacement
            .ClearFormatting
            .text = "<extended>^&</extended>"
        End With
        .Forward = True
        .Wrap = wdFindContinue
        '.MatchWildcards = True
        .Execute Replace:=wdReplaceAll
    End With	
    NovaaktFormateejumu ("<extended>")
    NovaaktFormateejumu ("</extended>")
    

End Sub

Sub Priekshdarbi()

    Dim oRange As Range
    NovaaktFormateejumu (" ^p")
    NovaaktFormateejumu ("^p")
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
