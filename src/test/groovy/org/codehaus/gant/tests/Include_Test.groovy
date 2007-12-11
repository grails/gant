//  Gant -- A Groovy build framework based on scripting Ant tasks.
//
//  Copyright © 2006-7 Russel Winder
//
//  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
//  compliance with the License. You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software distributed under the License is
//  distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
//  implied. See the License for the specific language governing permissions and limitations under the
//  License.

package org.codehaus.gant.tests

////////////////////////////////////////////////////////////////////////////////////////////////////
//  NB Commented out tests are ones that it is not certain should be supported.
////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 *  A test to ensure that the various include mechanisms work as they should.
 *
 *  @author Russel Winder <russel.winder@concertant.com>
 */
final class Include_Test extends GantTestCase {
  def toolClassName = 'ToolClass'
  def toolClassFilePath = "/tmp/${toolClassName}.groovy"
  def toolClassText =  """
class ${toolClassName} {
  def ${toolClassName} ( Binding binding ) { }
  def flob ( ) { println ( 'flobbed.' ) }
}
"""
  def toolBuildScriptBase =  """
target ( something : '' ) { ${toolClassName}.flob ( ) }
target ( 'default' : '' ) { something ( ) }
"""
  def toolBuildScriptClass =  "includeTool <<  groovyShell.evaluate ( '''${toolClassText} ; return ${toolClassName}''' )\n" + toolBuildScriptBase
  def toolBuildScriptFile =  "includeTool <<  new File ( '${toolClassFilePath}' )\n" + toolBuildScriptBase
  def toolBuildScriptString =  "includeTool <<  '''${toolClassText}'''\n" + toolBuildScriptBase
  def targetsScriptFilePath = '/tmp/targets.gant'
  def targetsScriptText =  '''
target ( flob : '' ) { println ( 'flobbed.' ) }
''' 
  def targetsClassName = 'TargetsClass'
  def targetsClassFilePath = "/tmp/${targetsClassName}.groovy"
  def targetsClassText =  """
class ${targetsClassName} {
  def ${targetsClassName} ( Binding binding ) {
    binding.target.call ( flob : '' ) { println ( 'flobbed.' ) }
  }
}
"""
  def targetsBuildScriptBase =  """
target ( something : '' ) { flob ( ) }
target ( 'default' : '' ) { something ( ) }
"""
  def targetsBuildScriptClass =  "includeTargets <<  groovyShell.evaluate ( '''${targetsScriptText} ; return ${targetsClassName}''' , ${targetsClassName} )\n" + targetsBuildScriptBase
  def targetsBuildScriptFile =  "includeTargets <<  new File ( '${targetsScriptFilePath}' )\n" + targetsBuildScriptBase
  def targetsBuildScriptString =  "includeTargets <<  '''${targetsScriptText}'''\n" + targetsBuildScriptBase
  def targetsBuildClassClass =  "includeTargets <<  groovyShell.evaluate ( '''${targetsClassText} ; return ${targetsClassName}''' )\n" + targetsBuildScriptBase
  def targetsBuildClassFile =  "includeTargets <<  new File ( '${targetsClassFilePath}' )\n" + targetsBuildScriptBase
  def targetsBuildClassString =  "includeTargets <<  '''${targetsClassText}'''\n" + targetsBuildScriptBase
  def nonExistentFilePath = '/tmp/tmp/tmp'
  Include_Test ( ) {
    ( new File ( toolClassFilePath ) ).write( toolClassText )
    ( new File ( targetsScriptFilePath ) ).write( targetsScriptText )
    ( new File ( targetsClassFilePath ) ).write( targetsClassText )
  }
  void testToolDefaultClass ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptClass ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  ] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  void testToolDefaultFile ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptFile ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  ] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  void testToolDefaultString ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptString ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  ] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  void testToolFlobClass ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptClass ) )
    assertEquals ( 11 , gant.process ( [ '-f' ,  '-'  , 'flob'] as String[] ) )
    assertEquals ( 'Target flob does not exist.\n' , output ) 
  }
  void testToolFlobFile ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptFile ) )
    assertEquals ( 11 , gant.process ( [ '-f' ,  '-'  , 'flob'] as String[] ) )
    assertEquals ( 'Target flob does not exist.\n' , output ) 
  }
  void testToolFlobString ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptString ) )
    assertEquals ( 11 , gant.process ( [ '-f' ,  '-'  , 'flob'] as String[] ) )
    assertEquals ( 'Target flob does not exist.\n' , output ) 
  }
  void testToolBurbleClass ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptClass ) )
    assertEquals ( 11 , gant.process ( [ '-f' ,  '-'  , 'burble'] as String[] ) )
    assertEquals ( 'Target burble does not exist.\n' , output ) 
  }
  void testToolBurbleFile ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptFile ) )
    assertEquals ( 11 , gant.process ( [ '-f' ,  '-'  , 'burble'] as String[] ) )
    assertEquals ( 'Target burble does not exist.\n' , output ) 
  }
  void testToolBurbleString ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptString ) )
    assertEquals ( 11 , gant.process ( [ '-f' ,  '-'  , 'burble'] as String[] ) )
    assertEquals ( 'Target burble does not exist.\n' , output ) 
  }
  void testToolSomethingClass ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptClass ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  , 'something'] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  void testToolSomethingFile ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptFile ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  , 'something'] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  void testToolSomethingString ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptString ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  , 'something'] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  void testToolClassNoFile ( ) {
    System.setIn ( new StringBufferInputStream ( toolBuildScriptFile.replace ( toolClassFilePath , nonExistentFilePath ) ) )
    assertEquals ( 2 , gant.process ( [ '-f' ,  '-'  , 'flob'] as String[] ) )
    assertEquals ( 'Standard input, line 1 -- ' + nonExistentFilePath + ' (No such file or directory)\n' , output )
  }
  void testTargetsDefaultClassClass ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassClass ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  ] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  /*
  void testTargetsDefaultClassFile ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassFile ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  ] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  void testTargetsDefaultClassString ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassString ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  ] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  */
  void testTargetsFlobClassClass ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassClass ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  , 'flob'] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  /*
  void testTargetsFlobClassFile ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassFile ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  , 'flob'] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  void testTargetsFlobClassString ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassString ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  , 'flob'] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  */
  void testTargetsBurbleClassClass ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassClass ) )
    assertEquals ( 11 , gant.process ( [ '-f' ,  '-'  , 'burble'] as String[] ) )
    assertEquals ( 'Target burble does not exist.\n' , output ) 
  }
  /*
  void testTargetsBurbleClassFile ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassFile ) )
    assertEquals ( 11 , gant.process ( [ '-f' ,  '-'  , 'burble'] as String[] ) )
    assertEquals ( 'Target burble does not exist.\n' , output ) 
  }
  void testTargetsBurbleClassString ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassString ) )
    assertEquals ( 11 , gant.process ( [ '-f' ,  '-'  , 'burble'] as String[] ) )
    assertEquals ( 'Target burble does not exist.\n' , output ) 
  }
  */
  void testTargetsSomethingClassClass ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassClass ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  , 'something'] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  /*
  void testTargetsSomethingClassFile ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassFile ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  , 'something'] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  void testTargetsSomethingClassString ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassString ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  , 'something'] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  */
  void testTargetsClassNoFile ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildClassFile.replace ( targetsClassFilePath , nonExistentFilePath ) ) )
    assertEquals ( 2 , gant.process ( [ '-f' ,  '-'  , 'flob'] as String[] ) )
    //  This is a weird message, should be better than this.
    assertEquals ( 'Standard input, line 1 -- ' + nonExistentFilePath + ' (' + nonExistentFilePath + ')\n' , output )
  }
  /*
  void testTargetsDefaultScriptClass ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptClass ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  ] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  */
  void testTargetsDefaultScriptFile ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptFile ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  ] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  void testTargetsDefaultScriptString ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptString ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  ] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  /*
  void testTargetsFlobScriptClass ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptClass ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  , 'flob'] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  */
  void testTargetsFlobScriptFile ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptFile ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  , 'flob'] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  void testTargetsFlobScriptString ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptString ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  , 'flob'] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  /*
  void testTargetsBurbleScriptClass ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptClass ) )
    assertEquals ( 11 , gant.process ( [ '-f' ,  '-'  , 'burble'] as String[] ) )
    assertEquals ( 'Target burble does not exist.\n' , output ) 
  }
  */
  void testTargetsBurbleScriptFile ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptFile ) )
    assertEquals ( 11 , gant.process ( [ '-f' ,  '-'  , 'burble'] as String[] ) )
    assertEquals ( 'Target burble does not exist.\n' , output ) 
  }
  void testTargetsBurbleScriptString ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptString ) )
    assertEquals ( 11 , gant.process ( [ '-f' ,  '-'  , 'burble'] as String[] ) )
    assertEquals ( 'Target burble does not exist.\n' , output ) 
  }
  /*
  void testTargetsSomethingScriptClass ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptClass ) )
    assertEquals ( 11 , gant.process ( [ '-f' ,  '-'  , 'something'] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  */
  void testTargetsSomethingScriptFile ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptFile ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  , 'something'] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  void testTargetsSomethingScriptString ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptString ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  , 'something'] as String[] ) )
    assertEquals ( 'flobbed.\n' , output ) 
  }
  void testTargetsScriptNoFile ( ) {
    System.setIn ( new StringBufferInputStream ( targetsBuildScriptFile.replace ( targetsScriptFilePath , nonExistentFilePath ) ) )
    assertEquals ( 2 , gant.process ( [ '-f' ,  '-'  , 'flob'] as String[] ) )
    //  This is a weird message, should be better than this.
    assertEquals ( 'Standard input, line 1 -- ' + nonExistentFilePath + ' (' + nonExistentFilePath + ')\n' , output )
  }

  ////////  Test multiple include of the same targets.

  void testTargetsMultipleIncludeDefaultScriptFile ( ) {
    System.setIn ( new StringBufferInputStream ( "includeTargets <<  new File ( '${targetsScriptFilePath}' )\n" + targetsBuildScriptFile ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  ] as String[] ) )
    assertEquals ( 'flobbed.\n' , output )
  }
  void testTargetsMultipleIncludeDefaultScriptString ( ) {
    System.setIn ( new StringBufferInputStream ( "includeTargets <<  '''${targetsScriptText}'''\n" + targetsBuildScriptString ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  ] as String[] ) )
    assertEquals ( 'flobbed.\n' , output )
  }
  void testTargetsMultipleIncludeFlobScriptFile ( ) {
    System.setIn ( new StringBufferInputStream ( "includeTargets <<  new File ( '${targetsScriptFilePath}' )\n" + targetsBuildScriptFile ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  , 'flob' ] as String[] ) )
    assertEquals ( 'flobbed.\n' , output )
  }
  void testTargetsMultipleIncludeFlobScriptString ( ) {
    System.setIn ( new StringBufferInputStream ( "includeTargets <<  '''${targetsScriptText}'''\n" + targetsBuildScriptString ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  , 'flob' ] as String[] ) )
    assertEquals ( 'flobbed.\n' , output )
  }
  void testTargetsMultipleIncludeBurbleScriptFile ( ) {
    System.setIn ( new StringBufferInputStream ( "includeTargets <<  new File ( '${targetsScriptFilePath}' )\n" + targetsBuildScriptFile ) )
    assertEquals ( 11 , gant.process ( [ '-f' ,  '-'  , 'burble'] as String[] ) )
    assertEquals ( 'Target burble does not exist.\n' , output ) 
  }
  void testTargetsMultipleIncludeBurbleScriptString ( ) {
    System.setIn ( new StringBufferInputStream ( "includeTargets <<  '''${targetsScriptText}'''\n" + targetsBuildScriptString ) )
    assertEquals ( 11 , gant.process ( [ '-f' ,  '-'  , 'burble'] as String[] ) )
    assertEquals ( 'Target burble does not exist.\n' , output ) 
  }
  void testTargetsMultipleIncludeSomethingScriptFile ( ) {
    System.setIn ( new StringBufferInputStream ( "includeTargets <<  new File ( '${targetsScriptFilePath}' )\n" + targetsBuildScriptFile ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  , 'flob' ] as String[] ) )
    assertEquals ( 'flobbed.\n' , output )
  }
  void testTargetsMultipleIncludeSomethingScriptString ( ) {
    System.setIn ( new StringBufferInputStream ( "includeTargets <<  '''${targetsScriptText}'''\n" + targetsBuildScriptString ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  , 'flob' ] as String[] ) )
    assertEquals ( 'flobbed.\n' , output )
  }

  void testUsingParameterConstructor ( ) {
    def theToolClassName = 'TheTool'
    def theToolClassText = """class ${theToolClassName} {
  ${theToolClassName} ( Binding binding , Map map ) { }
  def flob ( ) { println ( 'flobbed.' ) }
}"""
    System.setIn ( new StringBufferInputStream ( """includeTool ** groovyShell.evaluate ( '''${theToolClassText} ; return ${theToolClassName}''' ) * [ flob : 'adob' , foo : 'bar' ]
target ( something : '' ) { ${theToolClassName}.flob ( ) }
""" ) )
    assertEquals ( 0 , gant.process ( [ '-f' ,  '-'  , 'something' ] as String[] ) )    
    assertEquals ( 'flobbed.\n' , output )
  }
  
}
