# CiteQuranOXT
CiteQuranOXT is an open source LibreOffice extention under GPLv3 based on QuranLOAddon.
CiteQuranOXT redesigns the GUI and completes unfinished business of QuranLOAddon based on Java decompiling of [binary release](https://extensions.libreoffice.org/en/extensions/show/1102).
It adds a new feature that allows users to:

- Searching in the Quran text
- Show matching Ayat
- Select and insert a matching Aya in the current LibreOffice Writer document.
- By default, the inserted Quran text uses the **Amiri Quran Colored** font when available. 

We recommend to use one of the next two fonts in addition to the other ones suggested below in QuranLOAddon:

1. The **Amiri Quran** font, which is part of the **Amiri** arabic font family. Debian/Ubuntu like Linux distros users can install them with: **`sudo apt install fonts-hosny-amiri`**. Other systems users can download them from their [project webpage](https://github.com/aliftype/amiri) or the direct [link of version v1.000](https://github.com/aliftype/amiri/releases/download/1.000/Amiri-1.000.zip)
1. The **KFGQPC HAFS Uthmanic Script** font, which can be downloaded from its [webpage](https://fonts.qurancomplex.gov.sa/wp02/en/%D8%AD%D9%81%D8%B5/) in the Quran Printing Complex site or the direct [link of version v2.2](https://fonts.qurancomplex.gov.sa/wp02/wp-content/uploads/2024/01/UthmanicHafs_v22.zip). Unpack the downloaded zip and select the file **`UthmanicHafs_V22.ttf`**. Linux users can simply copy this file to `~/.fonts/truetype`

CiteQuran reuses qsearch (Quran Search) java module from the QuranKeyBoard Android [project](https://github.com/cdjalel/QuranKeyboard/tree/master) and [app](https://play.google.com/store/apps/details?id=com.djalel.android.qurankeyboard). 

*****

# QuranLOAddon
QuranLOAddon is a LibreOffice extension to add Qur'an text to a document. It allows you to select a complete 
surah or a range of ayats of a surah.

The standard font is selected from the LibreOffice Basic Fonts (CTL) setting. 
It uses the Default font and its Fontsize. The Font and Fontsize can be overridden by setting 
it on the selection dialog. The Arabic Font Selection box shows only fonts that support the Arabic characters on your system.

Arabic fonts that gives good results are: 
**Scheherazade**, **me_quran**, **Al Qalam Quran Majeed**, **Al Qalam Quran Majeed 1**, **Al Qalam Quran Majeed 2**. 

* **Scheherazade** This you can find at <https://software.sil.org/scheherazade/>.
Using so called smart features, you can fine tune the font. You can generate your own font with the smart features set to your liking. 

Other Arabic fonts give mixed results. Some don't have the parenthesis or even the standard Arabic numbers. I tried to mitigate for that by providing substitutions.

Urdu fonts that gives good results are: **Noto Nastaliq Urdu**


To build the extension I used the LibreOffice Eclipse plugin for extension development: 
<https://libreoffice.github.io/loeclipse/>. It also provides a starter project that you can use as an example. 

The Qur'an text and its translations were provided by <https://tanzil.net>:  

  Tanzil Quran Text (Uthmani, version 1.0.2)  
  Copyright (C) 2008-2010 Tanzil.net..  
  License: Creative Commons Attribution 3.0  
