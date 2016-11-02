<?xml version="1.0"?>

<!-- 
   Copyright (c) 2009, 2017 Mountainminds GmbH & Co. KG and Contributors
   All rights reserved. This program and the accompanying materials
   are made available under the terms of the Eclipse Public License v1.0
   which accompanies this distribution, and is available at
   http://www.eclipse.org/legal/epl-v10.html
  
   Contributors:
      Marc R. Hoffmann - initial API and implementation
-->

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml" exclude-result-prefixes="xdoc">

	<xsl:output method="xml" indent="yes" encoding="UTF-8"
		doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" />

	<xsl:param name="qualified.bundle.version" />
	<xsl:param name="jacoco.home.url" />
	<xsl:param name="copyright.years" />

	<xsl:template match="/">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
				<link rel="stylesheet" href="resources/doc.css" charset="UTF-8"
					type="text/css" />
				<link rel="shortcut icon" href="resources/report.gif" type="image/gif" />
				<title>
					JaCoCo - Command Line Interface
				</title>
			</head>
			<body>
				<div class="breadcrumb">
					<a href="../index.html" class="el_report">JaCoCo</a> &gt;
					<a href="index.html" class="el_group">Documentation</a> &gt;
					<span class="el_source">Command Line Interface</span>
				</div>
				<div id="content">
				
					<h1>Command Line Interface</h1>
					
					<p>
					  JaCoCo comes with a command line interface to perform
					  basic operations from the command line. The command line
					  tools with all dependencies are packaged in
					  <code>jacococli.jar</code> and are available with the
					  JaCoCo download. A Java VM with version 1.5 or greater is
					  required for execution.
					</p>
					
					<p>
					  For more sophisticated usage especially with larger
					  projects please use our
					  <a href="integrations.html">integrations</a> with various
					  build tools. 
					</p>
					<xsl:apply-templates select="documentation" />
				</div>
				<div class="footer">
					<span class="right">
						<a href="{$jacoco.home.url}">JaCoCo</a>
						&#160;
						<xsl:value-of select="$qualified.bundle.version" />
					</span>
					<a href="../doc/license.html">Copyright</a>
					&#169;
					<xsl:value-of select="$copyright.years" />
					Mountainminds GmbH &amp; Co. KG and Contributors
				</div>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="command">
		<h2><xsl:value-of select="@name" /></h2>
		<pre class="source">
			<xsl:value-of select="usage" />
		</pre>
		<p><xsl:value-of select="description" /></p>
		<table class="coverage">
			<thead>
				<tr>
					<td>Option</td>
					<td>Description</td>
					<td>Required</td>
				</tr>
			</thead>
			<tbody>
				<xsl:for-each select="option">
					<tr>
						<td><code><xsl:value-of select="usage" /></code></td>
						<td><xsl:value-of select="description" /></td>
						<td><xsl:choose>
							<xsl:when test="@required = 'true'">
								yes
							</xsl:when>
							<xsl:otherwise>
								no
							</xsl:otherwise>
						</xsl:choose></td>
					</tr>
				</xsl:for-each>
			</tbody>
		</table>
	</xsl:template>
	
</xsl:stylesheet>

