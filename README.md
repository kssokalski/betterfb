Na komputerach z systemem innym niż Windows, backend kompilowany do pliku .war za pomocą Maven 3.9.9 z folderu ./backend

<b><i>mvn clean install</b></i> 

Na komputerach z systemem innym niż Windows, frontend kompilowany z pomocą Node.js z folderu ./frontend/betterfb

<b><i>npm run build</b></i>

Z powodu używania Dockera, po każdej zmianie dobrze jest zbudować kontenery jeszcze raz z folederu ./backend

<b></i>docker-compose up -d --build<b></i>

Hasło do bazy dancych opisane w pliku docker-compose.yml. Aby korzystać z bazy danych w terminalu należy użyć komendy 

<b><i>docker exec -it betterfb-mysql-1 mysql -u root -p</b></i> 

W przypadku błędu związanego z backendem, należy sprawdzić na porcie konsolowym, czy <b>backend.war</b> został deployed. Jeżeli nie, to można go dodać ręcznie.

W przypadku błędu związanego z bazą danych, należy usunąć folder mysql z folderu data.
