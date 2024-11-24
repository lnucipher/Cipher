FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS base
WORKDIR /app

EXPOSE 3000

FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
ARG BUILD_CONFIGURATION=Development
WORKDIR /src

COPY ["Chat.Service/Chat.Api/Chat.Api.csproj", "Chat.Service/Chat.Api/"]
COPY ["Chat.Service/Chat.Application/Chat.Application.csproj", "Chat.Service/Chat.Application/"]
COPY ["Chat.Service/Chat.Domain/Chat.Domain.csproj", "Chat.Service/Chat.Domain/"]
COPY ["Chat.Service/Chat.Infrastructure/Chat.Infrastructure.csproj", "Chat.Service/Chat.Infrastructure/"]

RUN dotnet restore "Chat.Service/Chat.Api/Chat.Api.csproj"

COPY . .
WORKDIR "/src/Chat.Service/Chat.Api"
RUN dotnet build "Chat.Api.csproj" -c $BUILD_CONFIGURATION -o /app/build

FROM build AS publish
ARG BUILD_CONFIGURATION=Development
RUN dotnet publish "Chat.Api.csproj" -c $BUILD_CONFIGURATION -o /app/publish /p:UseAppHost=false

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .

ENTRYPOINT ["dotnet", "Chat.Api.dll"]
