# CiteQuranOXT
CiteQuranOXT is an open source LibreOffice extention under GPLv3 based on QuranLOAddon.
CiteQuranOXT redesigns the GUI and adds a new feature that allows users to:

- Searching in the Quran text
- Show matching Ayat
- Select and insert a matching Aya in the current LibreOffice Writer document.
- By default, the inserted Quran text uses the **Amiri Quran Colored** font when available. 

We strongly recommend to download and install the **Amiri Quran"** font.
Debian/Ubuntu like Linux distros users can install it with: **`sudo apt install fonts-hosny-amiri`**. Other systems users can download it from its [webpage](https://github.com/aliftype/amiri) or the direct [v1.000 link](https://github.com/aliftype/amiri/releases/download/1.000/Amiri-1.000.zip)

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
