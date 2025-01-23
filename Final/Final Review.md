## **Topics Covered (subject to change as time permits)**

- Android Basics
    - Apps and Activities
    - The Activity Lifecycle
    - MVC and Client/Server Model as it applies to Android
        - Remote authentication methods
        - Oauth
    - Common API calls
        - HTTP/S calls
        - SQLite calls
    - Multiprocessing (Threads vs AsyncTasks vs Services)
    - Interacting with Android via the CLI
- Basics of Android App Reverse Engineering
    - Extracting  information out of APKs
    - Auditing transport security using Burp Suite
    - Decompiling DEX (DAVLIK Executable Files)
    - Using MobSF
- Android OS & Security Internals
    - Access Control and Application Sandboxing
        - Users and Groups
            - Risks associated with jail-breaking and rooting
        - Permissions
            - Common permissions
            - Levels of permission
            - How permissions and groups relate
            - Process for granting access to permissions
        - Memory isolation and Binder
        - Storage isolation
    - Android File System Structure
        - /System
        - /data/system
        - /data/data
        - Notable files
            - Ex: The packages file
        - Shared storage areas
- Android App Code Signing
    - Android's app-level implementation of same origin policy (also called Trust on First Use)
    - Analyzing application signatures
    - Common code signing keys
        - System key, platform key, etc.
    - The implications of the lack of use of PKI in Android app code signing
    - The implications of Android app code signing on software updates
- Android Data Security
    - Shared preferences
    - Application-level Cryptography using Java crypto libraries
    - Key storage using Java keystores
    - Android Full-disk Encryption
        - Trusted Execution Environment (TEE)
- iOS Versus Android
    - iOS Encryption Model and the Secure Enclave
    - (As time permits) Basics of iOS application security testing [No lab]
- Android Application Security
    - OWASP Top 10 and OWASP Mobile Top 10 Vulnerabilities
    - Reverse Engineering Android apps and Bug Bounty Hunting
    - Trojaning Android Applications
- Mobile Networking and Messaging Protocols
    - GSM vs CDMA
    - Using Wireshark to inspect cellular traffic on emulated devices
    - SMS Protocol
    - iMessage Protocol
    - Signal Protocol
- Android Forensics
    - Accessing call and text information
    - Working with SQLite data databases
- Mobile Malware [As time permits]
    - Reverse engineering Android malware
    - Case studies in Android malware




## **Sample Exam**

You must answer the following questions:

1. Broadly describe Android application architecture and how android apps can be installed. Be sure to indicate a) the types components in an Android app, b) the role of the manifest file, c) how and in what form the application is usually distributed to the operating system, d) what the operating system does during installation, and e) where forensic evidence about most application usage can be found and the form in which that data is usually stored.

2. Describe the Android security model. Be sure to a) describe how the operating system implements and enforces sandboxing, b) identify how Android makes use of application code signing, c) how/when trust-on-first-use and same origin policy are implemented and verified, and d) where the information used by the Android security model to make security decisions can be found.

3. Discuss the importance of developer signing key management to Android security. Describe: a) why signing key management is important to Android’s security model, b) the signing key management implications of developing applications with shared user IDs, c) the challenges relating to signing keys posed to third-party developers, d) the implications of a compromised signing key, and e) the steps necessary to mitigate a compromised signing key.

Answer 6 out of the following 7 questions:

1. Evaluate the strengths and weaknesses of three different approaches to application-level credential management supported by the Android operating system and/or commonly observed in Android applications.

2. Describe, from start-to-finish, the process of encrypting and decrypting a file in Android. Be sure to include best practices for on-device key storage and describe how symmetric encryption would differ from asymmetric encryption.

3. Compare and contrast Android’s current approach to full-disk encryption and with its legacy approach to full disk encryption and with iOS’ approach to full disk encryption.

4. Describe the Android application security analysis process. Be sure to address the role of both static and dynamic analysis in evaluating Android application security. Be sure to identify common challenges to performing both static and dynamic analysis on Android applications.

5. Describe three Android-specific application-level vulnerabilities that are common Android vulnerabilities. For each vulnerability, be sure that you a) identify the level of access needed to exploit the vulnerability, b) describe the conditions under which the vulnerability is exploitable across a network and the conditions under which it only be exploitable locally, and c) identify the conditions under which this vulnerability would considered high impact, and d) explain how the vulnerability would be mitigated.

6. Encode “NEVER GONNA GIVE YOU UP” as an SMS message body as it would be transmitted in hexadecimal. Show each step of your work. Including a separate spreadsheet showing the work is allowed. Bonus points: Describe how SMS would transmit an audio file (this was not covered in class).

7. Compare and contrast the security of the SMS, iCloud, and Signal messaging protocols. Be sure to a) indicate if/when the messages are encrypted, b) describe how the messages are encrypted when they are encrypted, c) where the encryption keys for those messages reside and the circumstances under which the keys are transmitted across a network, and d) what steps would law enforcement or an attacker need to take in order compromise the keys? For d, you may describe the minimum attack path.