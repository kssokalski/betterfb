Backend kompilowany do pliku .war za pomocą Maven 3.9.9 - "mvn clean install" z folderu ./backend

Frontend kompilowany z pomocą Node.js - "npm run build" z folderu ./frontend/betterfb

Z powodu używania Dockera, po każdej zmianie dobrze jest zbudować kontenery jeszcze raz - "docker-compose up -d --build" z folederu ./backend

W przypadku błędu w dockerze o nieistniejącym pliku zmienić rodzaj odstępów w pliku wait-for-it.sh z CRLF na LF za pomocą używanego edytoru kodu
Używane porty opisane w pliku docker-compose.yml

Aby korzystać z bazy danych w terminalu należy użyć komendy - "docker exec -it betterfb-mysql-1 mysql -u root -p". Hasło opisane w pliku docker-compose.yml

Trzeba uważać przy testowaniu rejestracji. Logi dockera mówiły mi o tym, że program próbował tworzyć użytkowników w istniejące ID uzytkowników. Dopiero po kilku próbach zaczął wrzucać w puste ID.
