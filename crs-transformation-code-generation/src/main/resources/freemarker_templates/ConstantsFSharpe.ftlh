namespace ${nameOfPackageOrNamespace}
(* 
* Copyright (c) Tomas Johansson ( http://programmerare.com )
* The above person is author of the code that generated this class 
* with F# constants with EPSG codes for Coordinate Reference Systems.
* It is released with license MIT.
* Regarding the data itself: Ownership of the EPSG Dataset by IOGP is acknowledged.
* http://www.epsg.org/TermsOfUse.aspx
* Some quotes from the above 'Terms of use' URL:
* " The EPSG Facilities are published by IOGP at no charge. 
*   Distribution for profit is forbidden ... 
*   Ownership of the EPSG Dataset by IOGP must be acknowledged... "
*)

/// <summary>
<#list rowsForClassLevelComment as rowForClassLevelComment>
/// ${rowForClassLevelComment}
</#list>
/// </summary>
module ${nameOfClass} = 
    <#t>
    <#list constants as constant>
    
    (*
    * ${constant.documentationCommentForConstant}
    * https://epsg.io/${constant.epsgNumber?c}
    *)
    [<Literal>] 
    let ${constant.nameForConstant} = ${constant.valueForConstant?no_esc}        
    </#list>
