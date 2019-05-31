const express=require('express');
const cors=require('cors');
const bodyParser=require('body-parser');
require('./DB/connection');
const User=require('./Model/user');
const Item=require('./Model/item');
const app=express();
const path=require('path');
const multer=require('multer');
app.use(express.json());
app.use(cors());

app.use(bodyParser.urlencoded({extended:false}));
app.use("/images", express.static("images"))



app.post('/registeruser',(req,res)=>
{
    console.log(req.body);
    var ufname=req.body.userFname;
    var ulname=req.body.userLname;
    var uname=req.body.username;
    var pass=req.body.password;
    var mydata=new User({
        'userFname':ufname,
        'userLname':ulname,
        'username':uname,
        'password':pass

    });
    User.find({'username':uname}).count(function(err,number){
        if(number!=0){
        res.send('username already exists');
        console.log('Username already exists');
        }
        else{
    mydata.save().then(function()
    {
        res.json(mydata);               
    }).catch(function(e)
    {
        res.json(e);
    })
            }
})
})



app.post('/insertitem',(req,res)=>
{
    console.log(req.body);
    var itemname=req.body.itemName;
    var itemprice=req.body.itemPrice;
    var itemimagename=req.body.itemImageName;
    var itemdescription=req.body.itemDescription;
    var data=new Item({
        'itemName':itemname,
        'itemPrice':itemprice,
        'itemImageName':itemimagename,
        'itemDescription':itemdescription

    })
   

    data.save().then(function()
    {
        res.send(itemdata);               
    }).catch(function(e)
    {
        res.send(e);
    })
})

app.get('/selectitem',function(req,res){
    Item.find().then(function(data){
        res.send(data);
    }).catch(function(e)
    {
        res.send(e)
    })
})
app.get('/selectitembyId/:id',function(req,res){
    Item.findById(req.params.id).then(function(){
        res.send(data);
    }).catch(function(e)
    {
        res.send(e)
    })
})

app.post('/login',function(req,res){
    console.log(req.body);

    var uname=req.body.username;
    var pass=req.body.password;
    
    User.findOne({'username':uname, 'password':pass}).count(function(err,number){
        if(number!=0){
            res.statusCode=200;
            res.setHeader('Content-Type','application/json');
            res.json("Login successfull");
           
        
        }
        else{
            res.send('username and password mismatch');
            console.log('Username and password mismatch');
        }
    })
})

var storage = multer.diskStorage({
    destination: "images",
    filename: function(req, file, callback) {
        const ext = path.extname(file.originalname);
        callback(null, "Ushan" + Date.now() + ext);
    }

});
var imageFileFilter = (req, file, cb) => {
    if (!file.originalname.match(/\.(jpg|jpeg|png|gif)$/)) { return cb(new Error("You can upload only image files!"), false); }
    cb(null, true);
};

var upload = multer({
    storage: storage,
    fileFilter: imageFileFilter,
    limits: {
        fileSize: 10000000
    }
});

app.post('/uploadimg', upload.single('ImageName'), (req, res) => {
    res.json(req.file.filename)
})

app.get('/showItems', function(req,res){
    Item.find().then(function(allItemsData){
        var itemsData =JSON.stringify(allItemsData);
        console.log(itemsData);
        res.writeHead(200,{'Content-Type':'application/json'});
        res.end(itemsData);
    }).catch(function(error){

    })
})


app.listen(6060);
