# SBT Encryption Plugin
SBT plugin for typesafe configuration encryption.

At the moment only KMS encryption is supported. 

### HOWTO
Firstly, in order to encrypt our configuration files, we should tag those blocks that we want to encrypt/decrypt with the field `isEncrypted: true`.

```conf
foo {
	bar {
		isEncrypted: true
		value: 1
	}
}
``` 

Secondly, we need to add the plugin to our project.
```scala
addSbtPlugin("uk.co.telegraph"    % "sbt-encryption-plugin"  % "0.1.0-b+")
```

Thirdly, we need to encrypt our `application.conf` file.
The command requires the keyId used in the encryption process, the source file and the output file. Here is an example:
```bash
sbt "encrypt key-id /tmp/application.conf /tmp/application.enc.conf"
```

If you are using `KMS` the following script would find the key-id out of the key description.
```bash
for var in $(aws kms list-keys | grep KeyId | tr -s ' ' | sed -e 's/"KeyId": "//' | sed -e 's/",//')
do
   keyDesc=$(aws kms describe-key --key-id $var)
   isKey=$(echo $keyDesc | grep "Foo")
   if [ ! -z "$isKey" ]
   then
    keyId=$(echo $keyDesc | sed -nE 's/.*"KeyId": "([^"]*).*/\1/p')
    echo "--- Encrypting with key $keyId ---"
    sbt "encrypt $keyId /tmp/application.conf /tmp/application.enc.conf"
   fi
done
```

Once the file is encrypted, it can be decrypted using the following command:
```bash
sbt "decrypt /tmp/application.enc.conf /tmp/application.dec.conf"
```
