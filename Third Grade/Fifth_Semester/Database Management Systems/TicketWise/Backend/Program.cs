using Microsoft.EntityFrameworkCore;
using Ticketwise.Contexts;

var builder = WebApplication.CreateBuilder(args);
//builder.WebHost.UseUrls("http://172.20.10.13:5242");


// Adding the ApplicationDbContext to the services with SQL Server configuration
builder.Services.AddDbContext<ApplicationDbContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));


// Adding MVC controllers with views to the services
builder.Services.AddControllersWithViews();
builder.Services.AddControllers();
builder.Logging.AddConsole();

builder.Services.AddDistributedMemoryCache();

// Configuring session state with a 30-minute timeout and essential cookies
builder.Services.AddSession(options =>
{
    options.IdleTimeout = TimeSpan.FromMinutes(30);  
    options.Cookie.HttpOnly = true;  
    options.Cookie.IsEssential = true;  
});

// Adding CORS policy to allow any origin, method, and header
builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowAll", builder =>
        builder.AllowAnyOrigin()
               .AllowAnyMethod()
               .AllowAnyHeader());
});

// Adding controllers and configuring JSON serializer settings to ignore reference loops
builder.Services.AddControllers().AddNewtonsoftJson(options =>
{
    options.SerializerSettings.ReferenceLoopHandling = Newtonsoft.Json.ReferenceLoopHandling.Ignore;
});


var app = builder.Build();
app.UseSession();
app.UseCors("AllowAll");
app.UseCors(policy =>
    policy.AllowAnyOrigin()
          .AllowAnyMethod()
          .AllowAnyHeader());

app.UseDeveloperExceptionPage(); 


if (app.Environment.IsDevelopment())
{
    app.UseDeveloperExceptionPage();
    
}
else
{
    app.UseExceptionHandler("/Home/Error");
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();
app.UseRouting();


app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Login}/{action=Login}/{id?}");

app.Run();
