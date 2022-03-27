# logUtil
FOR GRADLE
Step 1. Add it in your root build.gradle at the end of repositories：
   	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency：
	dependencies {
	        implementation 'com.github.Dayone123456:logUtil:Tag'
	}

FOR MAVEN
Step 1. Add it in your project
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

Step 2. Add the dependency：
	<dependency>
	    <groupId>com.github.Dayone123456</groupId>
	    <artifactId>logUtil</artifactId>
	    <version>Tag</version>
	</dependency>

Website for you：https://jitpack.io/#Dayone123456/logUtil

