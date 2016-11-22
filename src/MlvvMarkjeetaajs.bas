Attribute VB_Name = "MlvvMarkjeetaajs"
Sub Markjeetaajs()
'
' Markjeetaajs Macro
'
' �obr�d ir kaut k�da ap�aub�ma probl�ma ar tabul�m - t�s ieved bezgal�g�
' cikl� dubulto tagu ielic�ju.
    Application.ScreenUpdating = False
    
    Priekshdarbi
    Izrunas
    VienkaarshoTaguIeliceejs
    DubultoTaguIeliceejs
    MsgBox "Viss gatavs!", 0, "MLVV"
    Application.ScreenUpdating = True
End Sub
Sub Izrunas()
    Dim oRange As Range
    ' Sasod�ts aprisin�jums izrunu izg��anai
    
    ' Uzsv�rta zible
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(61598) ' a cappella
        .Replacement.text = "!"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(61590) ' amplu�
        .Replacement.text = "!"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    
    ' Platais e ar ~
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(7869) & ChrW(61606) ' acum�rs
        .Replacement.text = "e,~"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    ' Platais e ar ^
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(234) & ChrW(61606) ' aizb�gt/-b�gu
        .Replacement.text = "e,^^"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    ' Platais e ar \
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(232) & ChrW(61606) ' aiz�mt/-�em
        .Replacement.text = "e,\"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    ' Platais � ar \
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = "�" & ChrW(61606) & ChrW(61592) ' aizcirst/-c�rtu
        .Replacement.text = "�,\"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = "�" & ChrW(61606) & ChrW(61661) ' apcirp/-c�rpu
        .Replacement.text = "�,\"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = "�" & ChrW(61661) & ChrW(61606) ' apsv�rums
        .Replacement.text = "�,\"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    
    ' Platais e
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = "e" & ChrW(61606) & ChrW(61606) ' aizvest/-vedu
        .Replacement.text = "e,"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = "e" & ChrW(61606) ' ac�mredzams
        .Replacement.text = "e,"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(553) ' aptaur�ts - k��da?
        .Replacement.text = "e,"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = "e" & ChrW(825) ' atsegums
        .Replacement.text = "e,"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    ' Platais �
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = "�" & ChrW(61606) ' aiz�epatot/aiz��patot
        .Replacement.text = "�,"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    ' Platais �
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = "�" & ChrW(825) ' aiz���rsot
        .Replacement.text = "�,"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    
    ' ~ (tilde)
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(227) ' adad�o
        .Replacement.text = "a~"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(7869) ' aizgriezt 1
        .Replacement.text = "e~"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(297) ' �bol�tis
        .Replacement.text = "i~"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = "m" & ChrW(61481) ' amplu�
        .Replacement.text = "m~"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(241) ' abiturents
        .Replacement.text = "n~"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = "n" & ChrW(61481) ' audience
        .Replacement.text = "n~"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(245) ' �bolains
        .Replacement.text = "o~"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(361) ' �bolk�ka
        .Replacement.text = "u~"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    
    ' ^ (circumflex)
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(226) ' �bolains
        .Replacement.text = "a^^"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(234) ' aizgriezt 2
        .Replacement.text = "e^^"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(238) ' �bolains
        .Replacement.text = "i^^"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(244) ' acotnis
        .Replacement.text = "o^^"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = "r" & ChrW(61481) ' adverbs
        .Replacement.text = "r^^"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = "r" & ChrW(61534) ' aizargjosla
        .Replacement.text = "r^^"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(251) ' ap�de�o�ana
        .Replacement.text = "u^^"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    
    ' \ (grave)
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(224) ' �bolmaize
        .Replacement.text = "a\"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    With oRange.Find
        .text = "�" & ChrW(61661) ' apk�rt 2
        .Replacement.text = "�\"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(232) ' a capella
        .Replacement.text = "e\"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(236) ' �boli��
        .Replacement.text = "i\"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(249) ' apbroc�gs
        .Replacement.text = "u\"
        .Forward = True
        .Wrap = wdFindContinue
        .Format = True
        .MatchCase = True
        .Execute Replace:=wdReplaceAll
    End With
    
    ' / (acute)

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
    
    ' Sasod�ts aprisin�jums tam, ka Word reiz�m nepareizi eksport� []
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
    ' Sasod�ts aprisin�jums tam, ka Word reiz�m nepareizi eksport� []
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
    Set oRange = ActiveDocument.Content
    With oRange.Find
        .text = ChrW(9633)
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
        .text = "�"
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
            ' Nesaprotu, k�p�c nenostr�d�, vajag aprisin�jumu.
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

