var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddDirectoryBrowser();

var app = builder.Build();

app.UseCors(options =>
	options.SetIsOriginAllowed(origin => true)
		.AllowAnyHeader()
		.AllowCredentials()
		.AllowAnyMethod());

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
	app.UseSwagger();
	app.UseSwaggerUI();

	app.UseDeveloperExceptionPage();
}
else
{
	//app.UseHttpsRedirection();
}

//app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.UseStaticFiles(); // Add this line

app.UseRouting();

app.Run();
