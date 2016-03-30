Attribute VB_Name = "MlvvMarkjeetaajs"
Sub Markjeetaajs()
'
' Markjeetaajs Macro
'
' Ðobrîd ir kaut kâda apðaubâma problçma ar tabulâm - tâs ieved bezgalîgâ
' ciklâ dubulto tagu ielicçju.
    Application.ScreenUpdating = False
    
    Priekshdarbi
    VienkaarshoTaguIeliceejs
    DubultoTaguIeliceejs
    MsgBox "Viss gatavs!", 0, "MLVV"
    Application.ScreenUpdating = True
End Sub

Sub VienkaarshoTaguIeliceejs()
    Dim oRange As Range
    
    Set oRange = ActiveDocument.Content
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
    Set oRange = ActiveDocument.Content
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
    Set oRange = ActiveDocument.Content
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
    
    Set oRange = ActiveDocument.Content
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
    Set oRange = ActiveDocument.Content
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
    
    Set oRange = ActiveDocument.Content
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
    
    Set oRange = ActiveDocument.Content
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
    Set oRange = ActiveDocument.Content
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
    
    Set oRange = ActiveDocument.Content
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
    
    Set oRange = ActiveDocument.Content
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
    Set oRange = ActiveDocument.Content
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
    Set oRange = ActiveDocument.Content
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
    Set oRange = ActiveDocument.Content
    ' Font = Wingdings 2
    With oRange.Find
        .text = ChrW(-3945)
        .Replacement.text = "<bullet/>"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .MatchWholeWord = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
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
    
    Set oRange = ActiveDocument.Content
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
        
    Set oldRange = ActiveDocument.Content
    With oldRange.Find
        .text = ""
        .Format = True
        .Font.Shading.BackgroundPatternColor = wdColorGray25
        Flag = .Execute
        With .Replacement
            .ClearFormatting
            .text = "<gray>^&</gray>"
        End With
        .Forward = True
        .Wrap = wdFindContinue
        '.MatchWildcards = True
        .Execute Replace:=wdReplaceAll
    End With
    
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ""
        .Format = True
        .Font.Shading.BackgroundPatternColor = wdColorGray50
        Flag = .Execute
        With .Replacement
            .ClearFormatting
            .text = "<gray>^&</gray>"
        End With
        .Forward = True
        .Wrap = wdFindContinue
        '.MatchWildcards = True
        .Execute Replace:=wdReplaceAll
    End With
    
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = "^p</gray>"
        .Replacement.text = "</gray>^p"
        .Forward = True
        .Wrap = wdFindContinue
        .Execute Replace:=wdReplaceAll
    End With

    Set oRange = ActiveDocument.Range
    With oRange
        .Find.Highlight = True
        .Find.Forward = True
        Do While oRange.Find.Execute
            If .HighlightColorIndex = WdColorIndex.wdGray25 And _
                    Not .Font.Shading.BackgroundPatternColor = wdColorGray50 And _
                    Not .Font.Shading.BackgroundPatternColor = wdColorGray25 Then
                .Find.Parent.InsertBefore "<gray>"
                .Find.Parent.InsertAfter "</gray>"
            End If
            intPosition = .End
            .Start = intPosition
        Loop
    End With
    
    Set oRange = ActiveDocument.Range
    With oRange
        .Find.Highlight = True
        .Find.Forward = True
        Do While oRange.Find.Execute
            If .HighlightColorIndex = WdColorIndex.wdGray50 And _
                    Not .Font.Shading.BackgroundPatternColor = wdColorGray50 And _
                    Not .Font.Shading.BackgroundPatternColor = wdColorGray25 Then
                .Find.Parent.InsertBefore "<gray>"
                .Find.Parent.InsertAfter "</gray>"
            End If
            intPosition = .End
            .Start = intPosition
        Loop
    End With
    NovaaktFormateejumu ("<gray>")
    NovaaktFormateejumu ("</gray>")
        
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
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(160)
        .Replacement.text = " "
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .MatchWholeWord = True
        .Execute Replace:=wdReplaceAll
    End With
    
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
        '.Replacement.Highlight = False
        With .Replacement.Font
            .Underline = False
            .Bold = False
            .Italic = False
            .Subscript = False
            .Superscript = False
            .Spacing = 0
            ' Nesaprotu, kâpçc nenostrâdâ, vajag aprisinâjumu.
            '.Shading.Texture = wdTextureNone
            '.Shading.ForegroundPatternColor = wdColorAutomatic
            '.Shading.BackgroundPatternColor = wdColorAutomatic
        End With
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .Execute Replace:=wdReplaceAll
    End With
End Sub

