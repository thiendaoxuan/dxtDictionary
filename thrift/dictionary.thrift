namespace cpp dictionaryService
namespace java com.vng.zing.dictionaryService.thrift
namespace php dictionaryService

enum ROLE {	
	ROLE_ADMIN = 0;
	ROLE_USER = 1;  
	NO_ROLE = 2; 
}

struct User
{
	1: required string userId;
	2: required string passwords;
	3: optional i32 role;
	
}


service DictionaryService
{
	string getTranslation(1:string word),
	set<string> getAutoCompletes(1:string word),
	set<string> getImage(1: string word,),
	bool updateTranslation (1:string word, 2:string translation),   
	bool deleteTranslation (1:string word), 
	i32 getAuthenLevel(1:string username, 2:string password),
	void addUser(1:string username, 2:string password, 3:i32 role), 
	i32 getReadTimes(1:string word),
	bool addReadTimes(1:string word, 2:i32 times),
	string getTopRead(),
}
