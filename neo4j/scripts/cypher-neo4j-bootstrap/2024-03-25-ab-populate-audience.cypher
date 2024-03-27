CREATE (audience1:Person {name:'client1', born:1971})
CREATE (audience2:Person {name:'client2', born:1968})
CREATE (audience3:Person {name:'client3', born:1957})
CREATE (audience4:Person {name:'client4', born:1943})
CREATE (audience5:Person {name:'client5', born:1967})
CREATE (audience6:Person {name:'client6', born:1948})
CREATE (audience7:Person {name:'client7', born:1947})
CREATE (audience8:Person {name:'client8', born:1961})
CREATE (audience9:Person {name:'client9', born:1962})
CREATE (audience10:Person {name:'client10', born:1937})
CREATE (audience11:Person {name:'client11', born:1962})
CREATE (audience12:Person {name:'client12', born:1958})
CREATE (audience13:Person {name:'client13', born:1966})

WITH audience1, audience2, audience3, audience4, audience5, audience6, audience7, audience8, audience9, audience10, audience11, audience12, audience13
MATCH (AFewGoodMen:Movie {title:'A Few Good Men'}), (TheMatrix:Movie {title:'The Matrix'}), (TheMatrixReloaded:Movie {title:'The Matrix Reloaded'}),
(TheMatrixRevolutions:Movie {title:'The Matrix Revolutions'}), (JerryMaguire:Movie {title:'Jerry Maguire'}), (TopGun:Movie {title:'Top Gun'})
CREATE
(audience9)-[:WATCHED {rating:1}]->(AFewGoodMen),
(audience10)-[:WATCHED {rating:1}]->(AFewGoodMen),
(audience11)-[:WATCHED {rating:2}]->(AFewGoodMen),
(audience12)-[:WATCHED {rating:1}]->(AFewGoodMen),
(audience13)-[:WATCHED {rating:0}]->(AFewGoodMen),
(audience1)-[:WATCHED {rating:1}]->(AFewGoodMen),
(audience2)-[:WATCHED {rating:2}]->(AFewGoodMen),
(audience3)-[:WATCHED {rating:1}]->(AFewGoodMen),
(audience4)-[:WATCHED {rating:0}]->(AFewGoodMen),
(audience5)-[:WATCHED {rating:1}]->(AFewGoodMen),
(audience6)-[:WATCHED {rating:1}]->(AFewGoodMen),
(audience8)-[:WATCHED {rating:2}]->(AFewGoodMen),

(audience9)-[:WATCHED {rating:1}]->(TheMatrix),
(audience10)-[:WATCHED {rating:1}]->(TheMatrix),
(audience11)-[:WATCHED {rating:2}]->(TheMatrix),
(audience12)-[:WATCHED {rating:1}]->(TheMatrix),
(audience13)-[:WATCHED {rating:0}]->(TheMatrix),
(audience1)-[:WATCHED {rating:1}]->(TheMatrix),
(audience2)-[:WATCHED {rating:2}]->(TheMatrix),
(audience3)-[:WATCHED {rating:1}]->(TheMatrix),
(audience4)-[:WATCHED {rating:0}]->(TheMatrix),
(audience5)-[:WATCHED {rating:1}]->(TheMatrix),
(audience6)-[:WATCHED {rating:1}]->(TheMatrix),
(audience8)-[:WATCHED {rating:2}]->(TheMatrix),

(audience9)-[:WATCHED {rating:1}]->(TheMatrixReloaded),
(audience10)-[:WATCHED {rating:1}]->(TheMatrixReloaded),
(audience11)-[:WATCHED {rating:2}]->(TheMatrixReloaded),
(audience12)-[:WATCHED {rating:1}]->(TheMatrixReloaded),
(audience13)-[:WATCHED {rating:0}]->(TheMatrixReloaded),
(audience1)-[:WATCHED {rating:1}]->(TheMatrixReloaded),
(audience2)-[:WATCHED {rating:2}]->(TheMatrixReloaded),
(audience3)-[:WATCHED {rating:1}]->(TheMatrixReloaded),
(audience4)-[:WATCHED {rating:0}]->(TheMatrixReloaded),
(audience5)-[:WATCHED {rating:1}]->(TheMatrixReloaded),
(audience6)-[:WATCHED {rating:1}]->(TheMatrixReloaded),
(audience8)-[:WATCHED {rating:2}]->(TheMatrixReloaded),

(audience9)-[:WATCHED {rating:1}]->(TheMatrixRevolutions),
(audience10)-[:WATCHED {rating:1}]->(TheMatrixRevolutions),
(audience11)-[:WATCHED {rating:2}]->(TheMatrixRevolutions),
(audience12)-[:WATCHED {rating:1}]->(TheMatrixRevolutions),
(audience13)-[:WATCHED {rating:0}]->(TheMatrixRevolutions),
(audience1)-[:WATCHED {rating:1}]->(TheMatrixRevolutions),
(audience2)-[:WATCHED {rating:2}]->(TheMatrixRevolutions),
(audience3)-[:WATCHED {rating:1}]->(TheMatrixRevolutions),
(audience4)-[:WATCHED {rating:0}]->(TheMatrixRevolutions),
(audience5)-[:WATCHED {rating:1}]->(TheMatrixRevolutions),
(audience6)-[:WATCHED {rating:1}]->(TheMatrixRevolutions),
(audience8)-[:WATCHED {rating:2}]->(TheMatrixRevolutions),

(audience9)-[:WATCHED {rating:1}]->(JerryMaguire),
(audience10)-[:WATCHED {rating:0}]->(JerryMaguire),
(audience11)-[:WATCHED {rating:2}]->(JerryMaguire),
(audience12)-[:WATCHED {rating:1}]->(JerryMaguire),
(audience13)-[:WATCHED {rating:0}]->(JerryMaguire),
(audience1)-[:WATCHED {rating:1}]->(JerryMaguire),
(audience2)-[:WATCHED {rating:2}]->(JerryMaguire),
(audience3)-[:WATCHED {rating:0}]->(JerryMaguire),
(audience4)-[:WATCHED {rating:0}]->(JerryMaguire),
(audience5)-[:WATCHED {rating:1}]->(JerryMaguire),
(audience6)-[:WATCHED {rating:1}]->(JerryMaguire),
(audience8)-[:WATCHED {rating:0}]->(JerryMaguire),

(audience9)-[:WATCHED {rating:1}]->(TopGun),
(audience10)-[:WATCHED {rating:2}]->(TopGun),
(audience11)-[:WATCHED {rating:2}]->(TopGun),
(audience12)-[:WATCHED {rating:1}]->(TopGun),
(audience13)-[:WATCHED {rating:0}]->(TopGun),
(audience1)-[:WATCHED {rating:0}]->(TopGun),
(audience2)-[:WATCHED {rating:2}]->(TopGun),
(audience3)-[:WATCHED {rating:2}]->(TopGun),
(audience4)-[:WATCHED {rating:0}]->(TopGun),
(audience5)-[:WATCHED {rating:0}]->(TopGun),
(audience6)-[:WATCHED {rating:1}]->(TopGun),
(audience8)-[:WATCHED {rating:2}]->(TopGun)

WITH audience1, audience2, audience3, audience4, audience5, audience6, audience7, audience8, audience9, audience10, audience11, audience12, audience13
MATCH (AsGoodAsItGets:Movie {title:'As Good as It Gets'}), (WhatDreamsMayCome:Movie {title:'What Dreams May Come'}), (SnowFallingonCedars:Movie {title:'Snow Falling on Cedars'}),
(YouveGotMail:Movie {title:"You've Got Mail"}), (SleeplessInSeattle:Movie {title:'Sleepless in Seattle'})
CREATE
(audience9)-[:WATCHED {rating:2}]->(AsGoodAsItGets),
(audience10)-[:WATCHED {rating:1}]->(AsGoodAsItGets),
(audience11)-[:WATCHED {rating:2}]->(AsGoodAsItGets),
(audience12)-[:WATCHED {rating:1}]->(AsGoodAsItGets),

(audience9)-[:WATCHED {rating:1}]->(WhatDreamsMayCome),
(audience10)-[:WATCHED {rating:2}]->(WhatDreamsMayCome),
(audience11)-[:WATCHED {rating:2}]->(WhatDreamsMayCome),
(audience12)-[:WATCHED {rating:1}]->(WhatDreamsMayCome),

(audience9)-[:WATCHED {rating:1}]->(SnowFallingonCedars),
(audience10)-[:WATCHED {rating:2}]->(SnowFallingonCedars),
(audience11)-[:WATCHED {rating:1}]->(SnowFallingonCedars),
(audience12)-[:WATCHED {rating:1}]->(SnowFallingonCedars),

(audience9)-[:WATCHED {rating:1}]->(YouveGotMail),
(audience10)-[:WATCHED {rating:2}]->(YouveGotMail),
(audience11)-[:WATCHED {rating:2}]->(YouveGotMail),
(audience12)-[:WATCHED {rating:2}]->(YouveGotMail),

(audience9)-[:WATCHED {rating:1}]->(SleeplessInSeattle),
(audience10)-[:WATCHED {rating:1}]->(SleeplessInSeattle),
(audience11)-[:WATCHED {rating:1}]->(SleeplessInSeattle),
(audience12)-[:WATCHED {rating:1}]->(SleeplessInSeattle)

WITH audience3, audience4, audience5, audience6, audience8
MATCH (TheReplacements:Movie {title:'The Replacements'}), (RescueDawn:Movie {title:'RescueDawn'}), (TheBirdcage:Movie {title:'The Birdcage'}),
(JohnnyMnemonic:Movie {title:'Johnny Mnemonic'}), (TheDaVinciCode:Movie {title:'The Da Vinci Code'})
CREATE
(audience3)-[:WATCHED {rating:1}]->(TheReplacements),
(audience4)-[:WATCHED {rating:1}]->(TheReplacements),
(audience5)-[:WATCHED {rating:1}]->(TheReplacements),
(audience6)-[:WATCHED {rating:1}]->(TheReplacements),
(audience8)-[:WATCHED {rating:2}]->(TheReplacements),

(audience3)-[:WATCHED {rating:2}]->(RescueDawn),
(audience4)-[:WATCHED {rating:0}]->(RescueDawn),
(audience5)-[:WATCHED {rating:1}]->(RescueDawn),
(audience6)-[:WATCHED {rating:1}]->(RescueDawn),
(audience8)-[:WATCHED {rating:2}]->(RescueDawn),

(audience3)-[:WATCHED {rating:0}]->(TheBirdcage),
(audience4)-[:WATCHED {rating:0}]->(TheBirdcage),
(audience5)-[:WATCHED {rating:1}]->(TheBirdcage),
(audience6)-[:WATCHED {rating:1}]->(TheBirdcage),
(audience8)-[:WATCHED {rating:0}]->(TheBirdcage),

(audience3)-[:WATCHED {rating:1}]->(JohnnyMnemonic),
(audience4)-[:WATCHED {rating:2}]->(JohnnyMnemonic),
(audience5)-[:WATCHED {rating:2}]->(JohnnyMnemonic),
(audience6)-[:WATCHED {rating:2}]->(JohnnyMnemonic),
(audience8)-[:WATCHED {rating:1}]->(JohnnyMnemonic),

(audience3)-[:WATCHED {rating:2}]->(TheDaVinciCode),
(audience4)-[:WATCHED {rating:1}]->(TheDaVinciCode),
(audience5)-[:WATCHED {rating:1}]->(TheDaVinciCode),
(audience6)-[:WATCHED {rating:1}]->(TheDaVinciCode),
(audience8)-[:WATCHED {rating:2}]->(TheDaVinciCode)
